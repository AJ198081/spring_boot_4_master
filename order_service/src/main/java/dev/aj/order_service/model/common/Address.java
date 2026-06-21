package dev.aj.order_service.model.common;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.SIMPLE_NAME, property = "type", include = JsonTypeInfo.As.EXTERNAL_PROPERTY)
public sealed interface Address {
    Country country();
    record Residential(String street, String city, String state, String postcode, Country country) implements Address { }
    record Commercial(String street, String city, String state, String postcode, Country country) implements Address { }
}
