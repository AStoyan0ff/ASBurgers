package START.Services;

import START.Exception.OrderAccessDeniedException;
import START.Exception.OrderNotPayableException;
import START.Enums.OrderStatus;
import START.Enums.PaymentStatus;
import START.Models.Order;
import START.Models.Payment;
import START.Repositories.OrderRepository;
import START.Repositories.PaymentRepository;
import START.Web.DTOs.PaymentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository,
                          OrderRepository orderRepository,
                          OrderService orderService) {

        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    public void payOrder(UUID orderId, UUID userId, PaymentRequest request) {
        Order order = orderService.getById(orderId);

        if (!order.getUser().getId().equals(userId)) {
            throw OrderAccessDeniedException.cannotPay();
        }

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new OrderNotPayableException();
        }

        Payment payment = Payment.builder()
                .order(order)
                .cardHolderName(request.getCardHolderName())
                .lastFourDigits(request.getCardNumber().substring(request.getCardNumber().length() - 4))
                .amount(order.getTotalPrice())
                .status(PaymentStatus.SUCCESSFUL)
                .paidAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);
        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);

    }
}
