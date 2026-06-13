package dev.aj.order_service.client;

import dev.aj.order_service.model.customer.Customer;

import java.util.UUID;

public interface CustomerClient {

    Customer getCustomer(UUID customerId);

}
