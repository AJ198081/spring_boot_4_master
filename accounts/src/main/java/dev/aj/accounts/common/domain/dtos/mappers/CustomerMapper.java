package dev.aj.accounts.common.domain.dtos.mappers;

import dev.aj.accounts.common.domain.dtos.CustomerRequest;
import dev.aj.accounts.common.domain.dtos.CustomerResponse;
import dev.aj.accounts.common.domain.entities.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customerId", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "accounts", ignore = true)
    @Mapping(target = "metaData", ignore = true)
    Customer toEntity(CustomerRequest customerRequest);

    CustomerResponse toResponse(Customer customer);

    @Mapping(target = "metaData", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customerId", ignore = true)
    @Mapping(target = "accounts", ignore = true)
    default void updateEntityFromRequest(CustomerRequest updateCustomerRequest, @MappingTarget Customer customer) {

        if (updateCustomerRequest.getFirstName() != null) {
            customer.setFirstName(updateCustomerRequest.getFirstName());
        }

        if (updateCustomerRequest.getLastName() != null) {
            customer.setLastName(updateCustomerRequest.getLastName());
        }

        if (updateCustomerRequest.getEmail() != null) {
            customer.setEmail(updateCustomerRequest.getEmail());
        }

        if (updateCustomerRequest.getPhoneNumber() != null) {
            customer.setPhoneNumber(updateCustomerRequest.getPhoneNumber());
        }
    }
}
