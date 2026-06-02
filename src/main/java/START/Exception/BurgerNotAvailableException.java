package START.Exception;

public class BurgerNotAvailableException extends ASBurgersException {

    public BurgerNotAvailableException() {
        super("Burger is not available.");
    }
}
