package dev.aj.bank_customer.migration.processors;

import dev.aj.bank_customer.migration.model.entities.NormalisedCustomerRecordEntity;
import dev.aj.bank_customer.migration.model.mappers.NormalisedCustomerMapper;
import dev.aj.bank_customer.model.entities.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@NullMarked
@RequiredArgsConstructor
@Slf4j
public class CustomerMigrationJDBCProcessor implements ItemProcessor<Customer, NormalisedCustomerRecordEntity> {

    private final NormalisedCustomerMapper normalisedCustomerMapper;

    @Override
    public @Nullable NormalisedCustomerRecordEntity process(Customer customer) {

        try {
            return normalisedCustomerMapper.customerToNormalisedCustomerRecordEntity(customer);
        } catch (Exception e) {
            log.error("Error processing customer: {}", customer, e);
            return null; // returning 'null' ensures that this particular customer is skipped
        }
    }
}
