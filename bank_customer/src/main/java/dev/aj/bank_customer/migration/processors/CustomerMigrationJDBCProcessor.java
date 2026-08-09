package dev.aj.bank_customer.migration.processors;

import dev.aj.bank_customer.migration.model.entities.NormalisedCustomerRecordEntity;
import dev.aj.bank_customer.migration.model.mappers.NormalisedCustomerMapper;
import dev.aj.bank_customer.model.entities.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

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

            switch (e) {

                case HttpClientErrorException clientError ->
                        log.error("HTTP error: {}", clientError.getResponseBodyAs(ProblemDetail.class));

                case HttpServerErrorException serverError ->
                        log.error("Server error: {}", serverError.getResponseBodyAs(ProblemDetail.class));

                case NullPointerException npe -> log.error("NullPointerException: {}", npe.getMessage());

                case IllegalArgumentException iae -> log.error("IllegalArgumentException: {}", iae.getMessage());

                default -> log.error("Unexpected error: {}", e.getMessage());
            }

            log.error("Error processing customer: {}", customer, e);

            return null; // returning 'null' ensures that this particular customer is skipped
        }
    }
}
