package dev.aj.bank_customer.migration.model.mappers;

import dev.aj.bank_customer.migration.model.entities.AddressEntity;
import dev.aj.bank_customer.migration.model.dtos.NormalisedCustomer;
import dev.aj.bank_customer.migration.model.entities.NormalisedCustomerEntity;
import dev.aj.bank_customer.model.entities.Address;
import dev.aj.bank_customer.model.entities.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@SuppressWarnings("unused")
public interface NormalisedCustomerMapper {

    @Mapping(target = "addressEntity", expression = "java(customerToAddressEntity(customer))")
    @Mapping(target = "normalisedCustomerEntity", expression = "java(customerToNormalisedCustomer(customer))")
    NormalisedCustomer map(Customer customer);

    NormalisedCustomerEntity customerToNormalisedCustomer(Customer customer);

    AddressEntity addressToAddressEntity(Address address);

    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "normalisedCustomerEntity", expression = "java(setNormalisedCustomerEntity(customerToNormalisedCustomer(customer)))")
    AddressEntity customerToAddressEntity(Customer customer);


}
