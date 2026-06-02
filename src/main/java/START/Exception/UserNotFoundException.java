package START.Exception;

public class UserNotFoundException extends ASBurgersException {

    public UserNotFoundException() {
        super("User not found.");
    }
}
