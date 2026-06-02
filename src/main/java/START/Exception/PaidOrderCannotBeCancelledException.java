package START.Exception;

public class PaidOrderCannotBeCancelledException extends ASBurgersException {

    public PaidOrderCannotBeCancelledException() {
        super("Paid order cannot be cancelled.");
    }
}
