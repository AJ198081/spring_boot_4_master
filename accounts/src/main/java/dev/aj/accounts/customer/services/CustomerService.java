package dev.aj.accounts.customer.services;

import dev.aj.accounts.common.domain.dtos.CustomerRequest;
import dev.aj.accounts.common.domain.dtos.CustomerResponse;

import java.util.UUID;

public interface CustomerService {
    CustomerResponse createCustomer(CustomerRequest customerData);

    CustomerResponse getCustomer(UUID customerId);

    void deleteCustomer(UUID customerId);

    void updateCustomer(UUID customerId, CustomerRequest updateCustomerRequest);
}
