package dev.aj.bank_user.model.types;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public record Password(String password) {

    private static final Predicate<String> HAS_MIN_LENGTH = password -> password.length() >= 8;

    private static final Predicate<String> HAS_MAX_LENGTH = password -> password.length() <= 50;

    //    ?= is a lookahead pattern, .*[0-9] -> from this position ahead, there must be digit
    private static final Predicate<String> HAS_DIGIT =
            Pattern.compile(".*[0-9].*").asMatchPredicate();

    private static final Predicate<String> HAS_LOWERCASE =
            Pattern.compile(".*[a-z].*").asMatchPredicate();

    private static final Predicate<String> HAS_UPPERCASE =
            Pattern.compile(".*[A-Z].*").asMatchPredicate();

    private static final Predicate<String> HAS_SPECIAL =
            Pattern.compile(".*[^a-zA-Z0-9].*").asMatchPredicate();

    public Password {

        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        if (HAS_DIGIT.negate().test(password)) {
            throw new IllegalArgumentException("Password must contain at least a number");
        }

        if (HAS_LOWERCASE.negate().test(password)) {
            throw new IllegalArgumentException("Password must have a lowercase alphabet");
        }

        if (HAS_UPPERCASE.negate().test(password)) {
            throw new IllegalArgumentException("Password must have an uppercase alphabet");
        }

        if (HAS_SPECIAL.negate().test(password)) {
            throw new IllegalArgumentException("Password must have a special character");
        }

        if (HAS_MIN_LENGTH.negate().test(password)) {
            throw new IllegalArgumentException("Password must be at least eight characters long");
        }

        if (HAS_MAX_LENGTH.negate().test(password)) {
            throw new IllegalArgumentException("Password must be at most 50 characters long");
        }
    }

}
