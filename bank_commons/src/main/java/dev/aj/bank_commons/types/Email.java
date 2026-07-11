package dev.aj.bank_commons.types;

import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public record Email(String email) {

    private static final Set<String> VALID_DOMAIN_NAMES = Set.of("com", "org", "edu", "in", "gov", "net", "au");

    private static final Predicate<String> IS_VALID_EMAIL_ADDRESS = Pattern.compile(
                    "^[a-zA-Z]+[a-zA-Z0-9+_.]+@[a-z.]+(" +
                            String.join("|", VALID_DOMAIN_NAMES)
                            + ")$")
            .asMatchPredicate();


    public Email {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }

        if (email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be blank");
        }

        if (!IS_VALID_EMAIL_ADDRESS.test(email)) {

            String errorMessage = VALID_DOMAIN_NAMES.stream()
                    .anyMatch(email::endsWith)
                    ? "Invalid Email format: %s".formatted(email)
                    : "Permitted email domains are: %s, received: %s".formatted(String.join(", ", VALID_DOMAIN_NAMES), email);

            throw new IllegalArgumentException(errorMessage);
        }
    }
}
