package dev.aj.order_service.model.order;

import org.jspecify.annotations.NonNull;

import java.util.UUID;

public record OrderExecution(UUID orderId, UUID customerId, String productId, int quantity) {

    public static OrderExecution create(@NonNull OrderRequest orderRequest) {
        return new OrderExecution(
                UUID.randomUUID(),
                orderRequest.customerId(),
                orderRequest.productId(),
                orderRequest.quantity()
        );
    }

}
