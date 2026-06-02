package START.Exception;

public class BurgerNotFoundException extends ASBurgersException {

    public BurgerNotFoundException() {
        super("Burger not found.");
    }
}
