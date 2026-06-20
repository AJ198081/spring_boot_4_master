package dev.aj.order_service.model.customer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.aj.order_service.model.common.ABN;
import dev.aj.order_service.model.common.Address;

import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.PROPERTY)
public sealed interface Customer {

    UUID id();
    String name();
    Address address();

    record RetailCustomer(UUID id, String name, Address address) implements Customer {
    }

    record WholesaleCustomer(UUID id, String name, Address address, ABN abn) implements Customer {
    }
}
