package dev.aj.bank_customer.model.mappers;

import dev.aj.commons.types.Email;
import dev.aj.bank_customer.model.dtos.AddressDto;
import dev.aj.bank_customer.model.dtos.CustomerCreatedResponse;
import dev.aj.bank_customer.model.dtos.CustomerRequest;
import dev.aj.bank_customer.model.dtos.CustomerResponse;
import dev.aj.bank_customer.model.entities.Address;
import dev.aj.bank_customer.model.entities.Customer;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

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


    Address toAddress(AddressDto addressRequest);

    AddressDto toAddress(Address address);

    default String emailToString(Email email) {
        return email.email();
    }

    default Email stringToEmail(String email) {
        return new Email(email);
    }

    Address.AddressType map(AddressDto.AddressType addressType);

    AddressDto.AddressType map(Address.AddressType addressType);

    @Mapping(target = "createdAt", source = "customer.auditMetaData.createdDate")
    CustomerResponse toCustomerResponse(Customer customer);

    default LocalDate toLocalDate(Date date) {

        LocalDate fromRdxRequestConverter = LocalDate.ofInstant(date.toInstant(), ZoneId.of("UTC"));
        LocalDate fromSB4MConverter = LocalDate.from(date.toInstant().atZone(ZoneId.systemDefault()));

        return fromRdxRequestConverter;
    }
}
