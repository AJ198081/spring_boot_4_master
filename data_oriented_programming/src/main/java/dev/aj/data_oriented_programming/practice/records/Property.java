package dev.aj.data_oriented_programming.practice.records;

public sealed interface Property {
    record Residential(Address address, int rooms) implements Property {}
    record Commercial(Address address, BusinessType businessType) implements Property {}
}


