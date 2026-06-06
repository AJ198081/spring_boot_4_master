package dev.aj.data_oriented_programming.practice.sealed_interfaces;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

public sealed interface PaymentProcessor {

    @Slf4j
    record CreditCard(String cardNumber, BigDecimal amount) implements PaymentProcessor {
        @Override
        public BigDecimal processPayment() {
            log.info("Processing payment for {}", this);
             return this.amount.multiply(BigDecimal.valueOf(1.1));
        }
    }

    @Slf4j
    record DebitCard(String cardNumber, BigDecimal amount) implements PaymentProcessor {
        @Override
        public BigDecimal processPayment() {
            log.info("Processing payment for {}", this);
            return amount.multiply(BigDecimal.valueOf(1.05));
        }
    }

    @Slf4j
    record Cash(BigDecimal amount) implements PaymentProcessor {
        @Override
        public BigDecimal processPayment() {
            log.info("Processing payment for {}", this);
            return amount;
        }
    }

    @Slf4j
    record PayPal(String paypalId, BigDecimal amount) implements PaymentProcessor {
        @Override
        public BigDecimal processPayment() {
            log.info("Processing payment for {}", this);
            return this.amount;
        }

    }

    BigDecimal processPayment();
}
