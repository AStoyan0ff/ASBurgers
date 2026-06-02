package START.Exception;

public class UsernameAlreadyExistsException extends RegistrationConflictException {

    public UsernameAlreadyExistsException() {
        super("Username already exists");
    }
}
