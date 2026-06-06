package dev.aj.data_oriented_programming.practice.records;

import org.jspecify.annotations.NonNull;

import java.util.List;

public record Person(@NonNull String firstName, @NonNull String lastName, List<@NonNull String> hobbies) {

//  Java complier will run the compact constructor before the default/canoncial constructor,
//  hence you won't be able to access `this` either

    public Person {
        if (firstName.isBlank() || lastName.isBlank()) {
            throw new IllegalArgumentException("First name and last name cannot be blank");
        }

        if (hobbies == null) {
            throw new IllegalArgumentException("Hobbies cannot be null");
        }

//        hobbies = List.copyOf(hobbies);
    }
}
