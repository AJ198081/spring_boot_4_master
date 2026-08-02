package dev.aj.bank_customer.migration.model.entities.persistenceConfigurations;

import dev.aj.bank_customer.migration.model.entities.NormalisedAddressRecordEntity;
import dev.aj.bank_customer.migration.model.entities.NormalisedCustomerRecordEntity;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.relational.core.mapping.event.BeforeConvertCallback;
import org.springframework.data.relational.core.mapping.event.BeforeSaveCallback;

import java.util.HashSet;
import java.util.Set;

@Configuration
@Slf4j
public class DatabaseConfiguration {

    @Bean
    public BeforeConvertCallback<NormalisedCustomerRecordEntity> beforeCustomerConvertCallback() {

        return (customerRecordEntity) -> {

            if (customerRecordEntity.id() == null) {
                log.error("Customer record entity id is null");
            }

            return assignIdentifiersToAddresses(customerRecordEntity);
        };
    }

//    @Bean
    public BeforeSaveCallback<NormalisedCustomerRecordEntity> beforeSaveCallback() {
        return (customerRecordEntity, changeEntity) -> assignIdentifiersToAddresses(customerRecordEntity);
    }

    private @NonNull NormalisedCustomerRecordEntity assignIdentifiersToAddresses(NormalisedCustomerRecordEntity customerRecordEntity) {
        Set<NormalisedAddressRecordEntity> updatedAddress = new HashSet<>();

        NormalisedAddressRecordEntity[] addresses = customerRecordEntity.addresses().toArray(new NormalisedAddressRecordEntity[0]);

        for (int i = 0; i < addresses.length; i++) {
            updatedAddress.add(assignIdentifierToAddressRecordIfMissing(addresses[i], customerRecordEntity, i));
        }

        return customerRecordEntity.withAddresses(updatedAddress);
    }

    private NormalisedAddressRecordEntity assignIdentifierToAddressRecordIfMissing(NormalisedAddressRecordEntity addressRecordEntity, NormalisedCustomerRecordEntity customerRecordEntity, int index) {

        if (addressRecordEntity.id() == null) {
            return addressRecordEntity.withId(Long.parseLong(String.valueOf(customerRecordEntity.id()).concat(String.valueOf(index))));
        }

        return addressRecordEntity;
    }

}
