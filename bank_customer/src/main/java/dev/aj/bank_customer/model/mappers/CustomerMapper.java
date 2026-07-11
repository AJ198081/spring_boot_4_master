package dev.aj.bank_customer.model.mappers;

import dev.aj.bank_commons.types.Email;
import dev.aj.bank_customer.model.dtos.AddressRequest;
import dev.aj.bank_customer.model.dtos.CustomerCreatedResponse;
import dev.aj.bank_customer.model.dtos.CustomerRequest;
import dev.aj.bank_customer.model.entities.Address;
import dev.aj.bank_customer.model.entities.Customer;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@SuppressWarnings("unused")
public interface CustomerMapper {

    @Mapping(target = "active", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "requestFingerPrint", ignore = true)
    @Mapping(target = "phone", source = "customerRequest.phoneNumber")
    @Mapping(target = "kycStatus", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalId", ignore = true)
    @Mapping(target = "auditMetaData", ignore = true)
//    @Mapping(target = "address", source = "customerRequest.address")
   Customer toEntity(CustomerRequest customerRequest);

    @Mapping(target = "createdAt", source = "customer.auditMetaData.createdDate")
    CustomerCreatedResponse toCreatedResponse(Customer customer);

    @Mapping(target = "active", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "requestFingerPrint", ignore = true)
    @Mapping(target = "phone", source = "customerRequest.phoneNumber")
    @Mapping(target = "kycStatus", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalId", ignore = true)
    @Mapping(target = "auditMetaData", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCustomer(CustomerRequest customerRequest, @MappingTarget Customer customer);

    default Address toAddress(AddressRequest addressRequest) {

        if (addressRequest == null) {
            return null;
        }

        return new Address(
                map(addressRequest.addressType()),
                addressRequest.streetNumber(),
                addressRequest.street(),
                addressRequest.city(),
                addressRequest.state(),
                addressRequest.postCode(),
                addressRequest.country()
        );
    }

    default String emailToString(Email email) {
        return email.email();
    }

    default Email stringToEmail(String email) {
        return new Email(email);
    }

    Address.AddressType map(AddressRequest.AddressType addressType);

}
