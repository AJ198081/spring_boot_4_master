package dev.aj.customer_service.service;

import dev.aj.order_service.model.customer.Customer;

import java.util.UUID;

public interface CustomerService {
    Customer getCustomerByIdentifier(UUID customerId);
}
