package dev.aj.data_oriented_programming.practice.records;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PersonTest {

    @Test
    void firstName() {
        Person person = new Person("John", "Doe", List.of("Java", "Python"));
        assertEquals("John", person.firstName());
    }

    @Test
    void lastName() {
        Person person = new Person("John", "Doe", List.of("Java", "Python"));
        assertEquals("Doe", person.lastName());
    }

    @Test
    void testCompactConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new Person("", "Doe", List.of("Java", "Python")));
        assertThrows(IllegalArgumentException.class, () -> new Person("John", "", List.of("Java", "Python")));
        assertThrows(IllegalArgumentException.class, () -> new Person(" ", "Doe", List.of("Java", "Python")));
        assertThrows(IllegalArgumentException.class, () -> new Person("John", " ", List.of("Java", "Python")));
        assertThrows(IllegalArgumentException.class, () -> new Person(" ", " ", List.of("Java", "Python")));

        assertThrows(NullPointerException.class, () -> new Person(null, "Doe", List.of("Java", "Python")));
        assertThrows(NullPointerException.class, () -> new Person("John", null, List.of("Java", "Python")));
    }

    @Test
    void testEquals() {
        Person person1 = new Person("John", "Doe", List.of("Java", "Python"));
        Person person2 = new Person("John", "Doe", List.of("Java", "Python"));
        assertEquals(person1, person2);

        Person person3 = new Person("John", "Doe", List.of("Java", "Python"));
        Person person4 = new Person("John", "Doe", List.of("Java", "Python"));
        assertNotSame(person1, person4);

        String s1 = "Hi";
        String s2 = "Hi";

        assertSame(s1, s2);
    }

    @Test
    void testFinalism() {
        Person person = new Person("Joe", "Doe", List.of("Java", "Python"));

        assertAll(
                () -> assertThrows(UnsupportedOperationException.class, () -> person.hobbies().add("C++")),
                () -> assertThrows(UnsupportedOperationException.class, () -> person.hobbies().remove("Java")),
                () -> assertThrows(UnsupportedOperationException.class, () -> person.hobbies().clear()),
                () -> assertThrows(UnsupportedOperationException.class, () -> person.hobbies().set(0, "C++"))
        );
    }

    @Test
    void testFinalismWhenMutableListIsUsed() {

        Person person = new Person("Joe", "Doe", Arrays.asList("Java", "Python"));

        List<@NonNull String> hobbies = person.hobbies();

        assertAll(
                () -> assertThrows(UnsupportedOperationException.class, () -> person.hobbies().add("C++"), () -> "List can't be extended"),
                () -> assertThrows(UnsupportedOperationException.class, () -> person.hobbies().remove("Java"), () -> "List can't be shrunk"),
                () -> assertThrows(UnsupportedOperationException.class, () -> person.hobbies().clear(), () -> "List can't be cleared"),
                () -> assertDoesNotThrow(() -> person.hobbies().set(0, "C++"), () -> "List can't be modified")
        );
    }
}