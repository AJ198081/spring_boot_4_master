package dev.aj.accounts.customer.services;

import dev.aj.accounts.common.domain.dtos.CustomerRequest;
import dev.aj.accounts.common.domain.dtos.CustomerResponse;
import dev.aj.accounts.common.exceptions.CustomerAlreadyExistsException;

import java.util.UUID;

public interface CustomerService {
    CustomerResponse createCustomer(CustomerRequest customerData) throws CustomerAlreadyExistsException;

    CustomerResponse getCustomer(UUID customerId);
}
