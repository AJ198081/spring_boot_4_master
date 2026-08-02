package dev.aj.bank_customer.migration.model.mappers;

import dev.aj.bank_customer.migration.model.dtos.NormalisedCustomer;
import dev.aj.bank_customer.migration.model.entities.AddressEntity;
import dev.aj.bank_customer.migration.model.entities.AuditMetaDataRecord;
import dev.aj.bank_customer.migration.model.entities.NormalisedAddressRecordEntity;
import dev.aj.bank_customer.migration.model.entities.NormalisedCustomerEntity;
import dev.aj.bank_customer.migration.model.entities.NormalisedCustomerRecordEntity;
import dev.aj.bank_customer.model.entities.Address;
import dev.aj.bank_customer.model.entities.AuditMetaData;
import dev.aj.bank_customer.model.entities.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@SuppressWarnings("unused")
public interface NormalisedCustomerMapper {

    @Mapping(target = "addressEntity", expression = "java(customerToAddressEntity(customer))")
    @Mapping(target = "normalisedCustomerEntity", expression = "java(customerToNormalisedCustomer(customer))")
    NormalisedCustomer map(Customer customer);

    @Mapping(target = "addresses", expression = "java(customerAddressToNormalisedAddressRecordEntities(customer))")
    NormalisedCustomerRecordEntity customerToNormalisedCustomerRecordEntity(Customer customer);

    NormalisedCustomerEntity customerToNormalisedCustomer(Customer customer);

    @Mapping(target = "auditMetaDataRecord", source = "auditMetaData")
    NormalisedAddressRecordEntity customerToNormalisedAddressRecordEntity(Customer customer);

    AddressEntity addressToAddressEntity(Address address);

    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "normalisedCustomerEntity", expression = "java(setNormalisedCustomerEntity(customerToNormalisedCustomer(customer)))")
    AddressEntity customerToAddressEntity(Customer customer);

    AuditMetaDataRecord auditMetaDataToAuditMetaDataRecord(AuditMetaData auditMetaData);

    default Set<NormalisedAddressRecordEntity> customerAddressToNormalisedAddressRecordEntities(Customer customer) {
        return Set.of(addressToNormalisedAddressRecordEntity(customer));
    }

    default NormalisedAddressRecordEntity addressToNormalisedAddressRecordEntity(Customer customer) {
        return new NormalisedAddressRecordEntity(
                null,
                customer.getAddress(),
                auditMetaDataToAuditMetaDataRecord(customer.getAuditMetaData())
        );
    }
}
