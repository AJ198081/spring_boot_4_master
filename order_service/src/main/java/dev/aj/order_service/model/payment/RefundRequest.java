package dev.aj.order_service.model.payment;

import dev.aj.order_service.model.common.NonNegativeAmount;

import java.util.UUID;

public record RefundRequest(UUID customerId, NonNegativeAmount amount, UUID transactionId) { }
