package START.Web.Controllers;

import START.Exception.ASBurgersException;
import START.Models.User;
import START.Services.BurgerService;
import START.Services.OrderService;
import START.Services.UserService;
import START.Web.DTOs.CreateOrderRequest;
import START.Web.DTOs.OrderItemRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final BurgerService burgerService;
    private final UserService userService;

    public OrderController(OrderService orderService, BurgerService burgerService, UserService userService) {

        this.orderService = orderService;
        this.burgerService = burgerService;
        this.userService = userService;
    }

    @GetMapping("/create")
    public ModelAndView createOrderPage(@RequestParam(required = false) UUID burgerId,
                                        HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");
        User user = userService.getById(userId);

        OrderItemRequest item = OrderItemRequest.builder()
                .burgerId(burgerId)
                .quantity(1)
                .build();

        CreateOrderRequest createOrderRequest = CreateOrderRequest.builder()
                .deliveryAddress(user.getAddress())
                .items(new ArrayList<>(java.util.List.of(item)))
                .build();

        ModelAndView mv = new ModelAndView("order-create");

        mv.addObject("createOrderRequest", createOrderRequest);
        mv.addObject("burgers", burgerService.getAllAvailableBurgers());
        mv.addObject("currentPage", "orders");

        return mv;
    }

    @PostMapping("/create")
    public ModelAndView createOrder(@Valid @ModelAttribute("createOrderRequest")
                                    CreateOrderRequest createOrderRequest,
                                    BindingResult bindingResult,
                                    HttpSession session) {

        UUID userId = (UUID) session.getAttribute("userId");

        if (bindingResult.hasErrors()) {
            return buildOrderCreateView(createOrderRequest, null);
        }

        try {
            orderService.createOrder(userId, createOrderRequest);

        } catch (ASBurgersException ex) {
            return buildOrderCreateView(createOrderRequest, ex.getMessage());
        }

        return new ModelAndView("redirect:/orders/my");
    }

    @GetMapping("/my")
    public ModelAndView myOrders(HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");

        ModelAndView mv = new ModelAndView("my-orders");

        mv.addObject("order", orderService.getCurrentPendingOrder(userId, null));
        mv.addObject("currentPage", "orders");

        return mv;
    }

    @GetMapping("/order-history")
    public ModelAndView orderHistory(HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");

        ModelAndView mv = new ModelAndView("order-history");

        mv.addObject("orders", orderService.getUserOrderHistory(userId));
        mv.addObject("currentPage", "order-history");

        return mv;
    }

    @PostMapping("/{id}/cancel")
    public ModelAndView cancelOrder(@PathVariable UUID id, HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");

        orderService.cancelOrder(id, userId);
        return new ModelAndView("redirect:/orders/my");
    }

    private ModelAndView buildOrderCreateView(CreateOrderRequest createOrderRequest, String orderError) {

        if (createOrderRequest.getItems() == null || createOrderRequest.getItems().isEmpty()) {
            createOrderRequest.setItems(new ArrayList<>(List.of(new OrderItemRequest())));
        }

        ModelAndView mv = new ModelAndView("order-create");

        mv.addObject("createOrderRequest", createOrderRequest);
        mv.addObject("burgers", burgerService.getAllAvailableBurgers());
        mv.addObject("currentPage", "orders");

        if (orderError != null) {
            mv.addObject("orderError", orderError);
        }

        return mv;
    }
}
