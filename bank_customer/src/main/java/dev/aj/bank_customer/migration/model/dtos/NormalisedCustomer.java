package dev.aj.bank_customer.migration.model.dtos;

import dev.aj.bank_customer.migration.model.entities.AddressEntity;
import dev.aj.bank_customer.migration.model.entities.NormalisedCustomerEntity;

public record NormalisedCustomer(NormalisedCustomerEntity normalisedCustomerEntity, AddressEntity addressEntity) {
}
