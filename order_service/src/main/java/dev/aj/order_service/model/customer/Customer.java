package dev.aj.order_service.model.customer;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.aj.order_service.model.common.ABN;
import dev.aj.order_service.model.common.Address;

import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.SIMPLE_NAME, property = "type", include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Customer.RetailCustomer.class, name = "RetailCustomer"),
        @JsonSubTypes.Type(value = Customer.WholesaleCustomer.class, name = "WholesaleCustomer")
})
public sealed interface Customer {

    UUID id();
    String name();
    Address address();

    record RetailCustomer(UUID id, String name, Address address) implements Customer {
    }

    record WholesaleCustomer(UUID id, String name, Address address, ABN abn) implements Customer {
    }
}
