package dev.aj.data_oriented_programming.practice.sealed_interfaces;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

public sealed interface Payment {

    @Slf4j
    record CreditCard(CardDetails cardDetails, BigDecimal amount) implements Payment {
    }

    @Slf4j
    record DebitCard(CardDetails cardDetails, BigDecimal amount) implements Payment {
    }

    @Slf4j
    record Cash(BigDecimal amount) implements Payment {
    }

    @Slf4j
    record PayPal(PayPalDetails payPalDetails, BigDecimal amount) implements Payment {
    }

    record CardDetails(String cardNumber, String cardHolderName, String expiryDate, String cvv) {
        public CardDetails {
            if (cardNumber.length() != 16) {
                throw new IllegalArgumentException("Card number must be 16 digits long");
            }
            if (cvv.length() != 3) {
                throw new IllegalArgumentException("CVV must be 3 digits long");
            }
            if (expiryDate.length() != 5) {
                throw new IllegalArgumentException("Expiry date must be in MM/YY format");
            }
            if (cardHolderName.isBlank()) {
                throw new IllegalArgumentException("Card holder name cannot be blank");
            }
        }
    }

    record PayPalDetails(String paypalId, String email, String firstName, String lastName) {
    }
}
