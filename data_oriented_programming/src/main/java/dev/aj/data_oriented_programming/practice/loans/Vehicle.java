package dev.aj.data_oriented_programming.practice.loans;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = Vehicle.Car.class, name = "Car"),
        @JsonSubTypes.Type(value = Vehicle.Motorcycle.class, name = "Motorcycle")
})
public sealed interface Vehicle {

    record Car(String make, String model, int year) implements Vehicle {}
    record Motorcycle(String make, String model, int engineCC, int year) implements Vehicle {}

}
