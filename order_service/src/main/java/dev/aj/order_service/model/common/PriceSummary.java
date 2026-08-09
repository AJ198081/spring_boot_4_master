package dev.aj.order_service.model.common;

public record PriceSummary(NonNegativeAmount totalPrice,
                           NonNegativeAmount discountedPrice,
                           NonNegativeAmount tax,
                           NonNegativeAmount orderPrice) {
}
