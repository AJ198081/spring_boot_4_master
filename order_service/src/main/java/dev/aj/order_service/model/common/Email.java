package dev.aj.order_service.model.common;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public record Email(String email) {

    private static final Predicate<String> IS_VALID_EMAIL_ADDRESS = Pattern.compile("^[a-zA-Z]+[a-zA-Z0-9+_.]+@[a-z.]+$")
            .asMatchPredicate();


    public Email {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }

        if (email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be blank");
        }

        if (!IS_VALID_EMAIL_ADDRESS.test(email)) {
            throw new IllegalArgumentException("Invalid email address");
        }
    }
}
