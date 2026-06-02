package START.Services;

import START.Exception.BurgerNotAvailableException;
import START.Exception.OrderAccessDeniedException;
import START.Exception.OrderNotFoundException;
import START.Exception.PaidOrderCannotBeCancelledException;
import START.Enums.OrderStatus;
import START.Models.Burger;
import START.Models.Order;
import START.Models.OrderItem;
import START.Models.User;
import START.Repositories.OrderItemRepository;
import START.Repositories.OrderRepository;
import START.Web.DTOs.CreateOrderRequest;
import START.Web.DTOs.OrderItemRequest;
import START.Web.DTOs.OrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserService userService;
    private final BurgerService burgerService;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                        UserService userService, BurgerService burgerService) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userService = userService;
        this.burgerService = burgerService;
    }

    public OrderResponse createOrder(UUID userId, CreateOrderRequest request) {
        User user = userService.getById(userId);

        Order order = new Order();
        order.setUser(user);
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setStatus(OrderStatus.CREATED);

        Order savedOrder = orderRepository.save(order);

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {
            Burger burger = burgerService.getById(itemRequest.getBurgerId());

            if (!burger.isAvailable()) {
                throw new BurgerNotAvailableException();
            }

            BigDecimal itemTotal = burger.getPrice()
                    .multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setBurger(burger);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(burger.getPrice());

            orderItemRepository.save(orderItem);

            totalPrice = totalPrice.add(itemTotal);
        }

        savedOrder.setTotalPrice(totalPrice);
        orderRepository.save(savedOrder);

        return mapToResponse(savedOrder);
    }

    public List<OrderResponse> getUserOrders(UUID userId) {
        User user = userService.getById(userId);

        return orderRepository.findAllByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<OrderResponse> getUserPendingOrders(UUID userId) {
        return getUserOrders(userId).stream()
                .filter(order -> order.getStatus() == OrderStatus.CREATED)
                .toList();
    }

    public List<OrderResponse> getUserOrderHistory(UUID userId) {
        return getUserOrders(userId).stream()
                .filter(order -> order.getStatus() != OrderStatus.CREATED)
                .toList();
    }

    public OrderResponse getCurrentPendingOrder(UUID userId, UUID preferredOrderId) {
        List<OrderResponse> pendingOrders = getUserPendingOrders(userId);

        if (pendingOrders.isEmpty()) {
            return null;
        }

        if (preferredOrderId != null) {
            return pendingOrders.stream()
                    .filter(order -> order.getId().equals(preferredOrderId))
                    .findFirst()
                    .orElse(pendingOrders.get(0));
        }

        return pendingOrders.get(0);
    }

    public Order getById(UUID id) {
        return orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
    }

    public void cancelOrder(UUID orderId, UUID userId) {
        Order order = getById(orderId);

        if (!order.getUser().getId().equals(userId)) {
            throw OrderAccessDeniedException.cannotCancel();
        }

        if (order.getStatus() == OrderStatus.PAID) {
            throw new PaidOrderCannotBeCancelledException();
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    private OrderResponse mapToResponse(Order order) {

        OrderResponse response = OrderResponse.builder()
                .id(order.getId())
                .deliveryAddress(order.getDeliveryAddress())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();

        return response;
    }

}
