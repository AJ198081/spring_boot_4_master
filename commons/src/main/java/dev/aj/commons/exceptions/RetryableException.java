package dev.aj.commons.exceptions;

@SuppressWarnings("unused")
public class RetryableException extends RuntimeException{

    public RetryableException(String message) {
        super(message);
    }

    public RetryableException(String message, Throwable cause) {
        super(message, cause);
    }

}
