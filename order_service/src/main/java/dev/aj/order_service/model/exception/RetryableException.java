package dev.aj.order_service.model.exception;

public class RetryableException extends ApplicationException {

    public RetryableException(SystemError error) {
        super(error);
    }
}
