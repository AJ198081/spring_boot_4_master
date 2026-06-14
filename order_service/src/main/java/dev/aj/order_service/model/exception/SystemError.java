package dev.aj.order_service.model.exception;

@SuppressWarnings("unused")
public sealed interface SystemError extends ApplicationError {

    String service();

    record InternalServerError(String service, String message) implements SystemError {
    }

    record ExternalServerError(String service, String message) implements SystemError {
    }
}
