package dev.aj.order_service.model.exception;

import lombok.Getter;

public class ApplicationException extends RuntimeException {

    @Getter
    private final ApplicationError error;

    public ApplicationException(ApplicationError error) {
        super(error.toString());
        this.error = error;
    }

}
