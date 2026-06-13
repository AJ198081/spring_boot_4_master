package dev.aj.order_service.model.exception;

public sealed interface SystemError extends ApplicationError {

    record InternalServerError(String service, String message) implements SystemError {
    }

    record ExternalServerError(String service, String message) implements SystemError {
    }
}
