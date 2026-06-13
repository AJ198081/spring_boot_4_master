package dev.aj.order_service.model.exception;

public sealed interface ApplicationError permits DomainError, SystemError {
}
