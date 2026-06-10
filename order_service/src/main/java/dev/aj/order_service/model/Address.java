package dev.aj.order_service.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.SIMPLE_NAME, property = "status", include = JsonTypeInfo.As.EXTERNAL_PROPERTY)
public sealed interface Address {
    record Residential(String street, String city, String state, String postcode) implements Address { }
    record Commercial(String street, String city, String state, String postcode) implements Address { }
}
