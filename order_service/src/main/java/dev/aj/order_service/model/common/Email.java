package dev.aj.order_service.model.common;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public record Email(String email) {

    private static final Predicate<String> IS_INVALID_EMAIL_ADDRESS = Pattern.compile("^[a-zA-Z]+[a-zA-Z_+0-9]+@[a-z.]{3}$")
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
