package dev.aj.data_oriented_programming.practice.loans;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = Property.Residential.class, name = "Residential"),
        @JsonSubTypes.Type(value = Property.Commercial.class, name = "Commercial")
})
public sealed interface Property {
    record Residential(Address address, int rooms) implements Property {}
    record Commercial(Address address, BusinessType businessType) implements Property {}
}


