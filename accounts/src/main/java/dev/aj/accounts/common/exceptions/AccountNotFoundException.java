package dev.aj.accounts.common.exceptions;

public class AccountNotFoundException extends Throwable {
    public AccountNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
