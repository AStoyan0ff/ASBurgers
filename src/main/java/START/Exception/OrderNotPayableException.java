package START.Exception;

public class OrderNotPayableException extends ASBurgersException {

    public OrderNotPayableException() {
        super("Only created orders can be paid.");
    }
}
