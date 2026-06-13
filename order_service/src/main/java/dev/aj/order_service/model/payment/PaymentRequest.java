package dev.aj.order_service.model.payment;

import dev.aj.order_service.model.common.NonNegativeAmount;

import java.util.UUID;

public record PaymentRequest(UUID customerId, UUID orderId, NonNegativeAmount amount) {
}

