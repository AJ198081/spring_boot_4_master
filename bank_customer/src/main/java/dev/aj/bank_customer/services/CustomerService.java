package dev.aj.bank_customer.services;

import dev.aj.bank_customer.model.dtos.CustomerCreatedResponse;
import dev.aj.bank_customer.model.dtos.CustomerRequest;
import dev.aj.bank_customer.model.dtos.CustomerResponse;
import dev.aj.bank_customer.model.entities.Customer;
import dev.aj.bank_customer.events.CustomerCreateEvent;

import java.util.UUID;

public interface CustomerService {
    CustomerCreatedResponse create(CustomerRequest customerRequest);

    Customer create(CustomerCreateEvent customerCreateEvent);

    UUID createAsync(CustomerRequest customerRequest);

    Short updateKycStatus(UUID customerId, String kycStatus);

    void updateKycStatusAsync(UUID customerExternalId, String kycStatus);

    CustomerResponse getCustomer(UUID customerExternalId);
}
