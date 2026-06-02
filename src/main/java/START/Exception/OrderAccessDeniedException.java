package START.Exception;

public class OrderAccessDeniedException extends ASBurgersException {

    public OrderAccessDeniedException(String message) {
        super(message);
    }

    public static OrderAccessDeniedException cannotCancel() {
        return new OrderAccessDeniedException("You cannot cancel this order.");
    }

    public static OrderAccessDeniedException cannotPay() {
        return new OrderAccessDeniedException("You cannot pay this order.");
    }
}
