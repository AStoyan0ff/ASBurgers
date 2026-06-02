package START.Exception;

public class EmailAlreadyExistsException extends RegistrationConflictException {

    public EmailAlreadyExistsException() {
        super("Email already exists");
    }
}
