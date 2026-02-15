package dev.aj.accounts.common.exceptions;

@SuppressWarnings("unused")
public class CustomerAlreadyExistsException extends Throwable {

    public CustomerAlreadyExistsException(String message) {
        super(message);
    }

    public CustomerAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
