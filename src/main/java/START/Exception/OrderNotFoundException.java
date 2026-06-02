package START.Exception;

public class OrderNotFoundException extends ASBurgersException {

    public OrderNotFoundException() {
        super("Order not found.");
    }
}
