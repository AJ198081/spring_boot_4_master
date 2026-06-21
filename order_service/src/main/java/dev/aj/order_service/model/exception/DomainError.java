package dev.aj.order_service.model.exception;

import dev.aj.order_service.model.common.NonNegativeAmount;

import java.util.UUID;

public sealed interface DomainError extends ApplicationError {

    enum Entity {
        ORDER, PRODUCT, CUSTOMER, PAYMENT, INVOICE, SHIPMENT
    }

    record EntityNotFound(Entity entity, String id) implements DomainError { }
    record EntityAlreadyExists(Entity entity, String id) implements DomainError { }
    record ProductDiscontinued(String productId) implements DomainError {
        public static ApplicationError of(String productID) {
            return new ProductDiscontinued(productID);
        }
    }
    record PaymentFailed(UUID orderId, UUID customerId, NonNegativeAmount amount, String reason) implements DomainError { }
    record MissingArgument(String argumentName) implements DomainError { }
    record InvalidResponseBody(String message) implements DomainError { }

}
