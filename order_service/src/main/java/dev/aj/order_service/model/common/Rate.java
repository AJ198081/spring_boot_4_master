package dev.aj.order_service.model.common;

public record Rate(double value) {

    public Rate {

        if (value > 1) {
            value = value / 100;
        }

        if (value <= 0) {
            throw new IllegalArgumentException("Rate must be a positive number.");
        }
    }
}
