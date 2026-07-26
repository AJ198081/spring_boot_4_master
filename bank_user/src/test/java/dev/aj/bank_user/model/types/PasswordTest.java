package dev.aj.bank_user.model.types;

import dev.aj.commons.types.Password;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordTest {

    @Test
    void testPasswordContainsAtLeastOneNumber() {
        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () -> new Password("passwordNoNumber"));
        Assertions.assertThat(thrownException.getMessage()).contains("number");
    }

    @Test
    void testPasswordContainsAtLeastOneLowerCaseLetter() {
        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () -> new Password("PASSWORD_NO_LOWER_CASE_LETTER_1"));
        Assertions.assertThat(thrownException.getMessage()).contains("lowercase");
    }

    @Test
    void testPasswordContainsAtLeastOneUpperCaseLetter() {
        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () -> new Password("password_no_upper_case_letter_1"));
        Assertions.assertThat(thrownException.getMessage()).contains("uppercase");
    }

    @Test
    void testPasswordContainsAtLeastOneSpecialCharacter() {
        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () -> new Password("passwordNoSpecialCharacter1"));
        Assertions.assertThat(thrownException.getMessage()).contains("special");
    }

    @Test
    void testViolatesMinLength() {
        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () -> new Password("pP123_"));
        Assertions.assertThat(thrownException.getMessage()).contains("at least");
    }

    @Test
    void testViolatesMaxLength() {
        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () -> new Password("pP1_".repeat(13)));
        Assertions.assertThat(thrownException.getMessage()).contains("at most");
    }

    @Test
    void testValidPassword() {
        Password password = new Password("lower_UPPER_9");
        assertNotNull(password);
    }

    @Test
    void testStartingWithNumberIsAValidPassword() {
        Password password = new Password("1lower!UPPER");
        assertNotNull(password);
    }



}