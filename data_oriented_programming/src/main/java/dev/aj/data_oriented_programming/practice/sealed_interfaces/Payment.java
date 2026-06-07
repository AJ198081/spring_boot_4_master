package dev.aj.data_oriented_programming.practice.sealed_interfaces;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Service
@Validated
public sealed interface Payment {

    @Slf4j
    record CreditCardPayment(CardDetails cardDetails, BigDecimal amount) implements Payment {
    }

    @Slf4j
    record DebitCardPayment(CardDetails cardDetails, BigDecimal amount) implements Payment {
    }

    @Slf4j
    record CashPayment(BigDecimal amount) implements Payment {
    }

    @Slf4j
    record PayPalPayment(PayPalDetails payPalDetails, BigDecimal amount) implements Payment {
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

    record PayPalDetails(PayPalIdentifier paypalId, @NonNull Email email, String firstName, String lastName) {
    }

    record PayPalIdentifier(UUID identifier) { }

    record Email(String email) {

        private static final Predicate<String> IS_INVALID_EMAIL_ADDRESS = Pattern.compile("^[a-zA-Z]+[a-zA-Z_+0-9]+@[a-z.]+$")
                .asMatchPredicate()
                .negate();

        public Email {
            if (email == null) {
                throw new IllegalArgumentException("Email cannot be null");
            }

            if (email.isBlank()) {
                throw new IllegalArgumentException("Email cannot be blank");
            }

            if (IS_INVALID_EMAIL_ADDRESS.test(email)) {
                throw new IllegalArgumentException("Invalid email address");
            }
        }
    }
}
