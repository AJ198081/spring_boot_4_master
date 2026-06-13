package dev.aj.order_service.model.common;

import java.math.BigDecimal;

public record NonNegativeAmount(BigDecimal amount) {
    public NonNegativeAmount {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be non-negative");
        }
    }
}
