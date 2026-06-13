package dev.aj.order_service.model.common;

public record PriceSummary(NonNegativeAmount subtotal,
                           NonNegativeAmount discountedPrice,
                           NonNegativeAmount tax,
                           NonNegativeAmount total) {
}
