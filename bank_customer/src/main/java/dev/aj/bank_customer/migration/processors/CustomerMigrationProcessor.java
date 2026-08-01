package dev.aj.bank_customer.migration.processors;

import dev.aj.bank_customer.migration.model.dtos.NormalisedCustomer;
import dev.aj.bank_customer.migration.model.entities.NormalisedCustomerEntity;
import dev.aj.bank_customer.migration.model.mappers.NormalisedCustomerMapper;
import dev.aj.bank_customer.model.entities.Customer;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@NullMarked
@RequiredArgsConstructor
public class CustomerMigrationProcessor implements ItemProcessor<Customer, NormalisedCustomerEntity> {

    private final NormalisedCustomerMapper normalisedCustomerMapper;

    @Override
    public @Nullable NormalisedCustomerEntity process(Customer customer) {

        try {
            NormalisedCustomer normalisedCustomer = normalisedCustomerMapper.map(customer);

            normalisedCustomer.addressEntity().setNormalisedCustomerEntity(
                    normalisedCustomer.normalisedCustomerEntity()
            );

            return normalisedCustomer.normalisedCustomerEntity();

        } catch (Exception e) {
            return null; // returning 'null' ensures that this particular customer is skipped
        }
    }
}
