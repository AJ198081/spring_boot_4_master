package dev.aj.accounts.common.exceptions;

public class CustomerNotFoundException extends Throwable {
    public CustomerNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
