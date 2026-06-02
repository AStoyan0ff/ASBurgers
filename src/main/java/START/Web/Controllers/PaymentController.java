package START.Web.Controllers;

import START.Exception.ASBurgersException;
import START.Services.OrderService;
import START.Services.PaymentService;
import START.Web.DTOs.PaymentRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderService orderService;

    public PaymentController(PaymentService paymentService, OrderService orderService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
    }

    @GetMapping("/{orderId}")
    public ModelAndView paymentPage(@PathVariable UUID orderId) {
        return new ModelAndView("redirect:/orders/my");
    }

    @PostMapping("/{orderId}")
    public ModelAndView payOrder(@PathVariable UUID orderId,
                                 @Valid @ModelAttribute PaymentRequest paymentRequest,
                                 BindingResult bindingResult,
                                 HttpSession session) {

        UUID userId = (UUID) session.getAttribute("userId");

        if (bindingResult.hasErrors()) {
            return buildMyOrdersView(userId, orderId, "Please check your card details.", paymentRequest);
        }

        try {
            paymentService.payOrder(orderId, userId, paymentRequest);

        } catch (ASBurgersException ex) {
            return buildMyOrdersView(userId, orderId, ex.getMessage(), paymentRequest);
        }

        return new ModelAndView("redirect:/orders/order-history");
    }

    private ModelAndView buildMyOrdersView(UUID userId,
                                           UUID failedOrderId,
                                           String paymentError,
                                           PaymentRequest paymentRequest) {

        ModelAndView mv = new ModelAndView("my-orders");

        mv.addObject("order", orderService.getCurrentPendingOrder(userId, failedOrderId));
        mv.addObject("currentPage", "orders");
        mv.addObject("failedOrderId", failedOrderId);
        mv.addObject("paymentError", paymentError);
        mv.addObject("paymentCardHolderName", paymentRequest.getCardHolderName());
        mv.addObject("paymentCardNumber", paymentRequest.getCardNumber());

        return mv;
    }
}
