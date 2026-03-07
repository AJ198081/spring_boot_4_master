package dev.aj.accounts.common.exceptions;

public class AccountAlreadyExistsException extends RuntimeException {
    public AccountAlreadyExistsException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
