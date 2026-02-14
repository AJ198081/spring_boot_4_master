package dev.aj.accounts.common.exceptions;

public class AccountAlreadyExistsException extends Throwable {
    public AccountAlreadyExistsException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
