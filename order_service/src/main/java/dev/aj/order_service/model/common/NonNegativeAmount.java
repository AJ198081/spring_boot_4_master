package dev.aj.order_service.model.common;

import java.math.BigDecimal;

public record NonNegativeAmount(BigDecimal amount) {
    public NonNegativeAmount {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be non-negative");
        }
    }

    public NonNegativeAmount add(NonNegativeAmount other) {
        return new NonNegativeAmount(this.amount.add(other.amount));
    }

    public static NonNegativeAmount ZERO() {
        return new NonNegativeAmount(BigDecimal.ZERO);
    }

    public static NonNegativeAmount of(BigDecimal amount) {
        return new NonNegativeAmount(amount);
    }

    public static NonNegativeAmount of(Rate rate) {
        return new NonNegativeAmount(BigDecimal.valueOf(rate.value()));
    }

    public NonNegativeAmount multiply(NonNegativeAmount otherAmount) {
        return new NonNegativeAmount(this.amount.multiply(otherAmount.amount));
    }

    public boolean greaterThan(NonNegativeAmount amount) {
        return this.amount.compareTo(amount.amount) > 0;
    }

    public NonNegativeAmount subtract(NonNegativeAmount subtrahend) {
        return new NonNegativeAmount(this.amount.subtract(subtrahend.amount));
    }

    public NonNegativeAmount percentage(Rate percentage) {
        return NonNegativeAmount.of(
                this.amount.multiply(
                        BigDecimal.valueOf(1.0 - percentage.value()))
        );
    }
}
