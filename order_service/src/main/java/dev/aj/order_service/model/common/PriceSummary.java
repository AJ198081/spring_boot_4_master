package dev.aj.order_service.model.common;

public record PriceSummary(NonNegativeAmount subtotal,
                           NonNegativeAmount discountApplied,
                           NonNegativeAmount tax,
                           NonNegativeAmount total) {
}
