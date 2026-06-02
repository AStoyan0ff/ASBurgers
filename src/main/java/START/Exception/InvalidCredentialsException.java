package START.Exception;

public class InvalidCredentialsException extends ASBurgersException {

    public InvalidCredentialsException() {
        super("Invalid username or password.");
    }
}
