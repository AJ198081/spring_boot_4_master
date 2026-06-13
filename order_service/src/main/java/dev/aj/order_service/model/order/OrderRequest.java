package dev.aj.order_service.model.order;

import jakarta.validation.constraints.Min;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public record OrderRequest(@NonNull UUID customerId, @NonNull String productId, @Min(1) int quantity) {
}
