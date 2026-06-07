package dev.aj.data_oriented_programming.practice.records;

public sealed interface Vehicle {

    record Car(String make, String model, int year) implements Vehicle {}
    record Motorcycle(String make, String model, int engineCC, int year) implements Vehicle {}

}
