package dev.aj.bank_customer.migration.steps;

import dev.aj.bank_customer.migration.model.entities.NormalisedCustomerEntity;
import dev.aj.bank_customer.model.entities.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.data.RepositoryItemReader;
import org.springframework.batch.infrastructure.item.database.JdbcCursorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ETLStepForMigration {

    @Bean
    public Step etlStepForMigration(
            JobRepository jobRepository,
            JdbcCursorItemReader<Customer> customerJdbcCursorItemReader,
            RepositoryItemReader<Customer> customerRepositoryItemReader,
            ItemProcessor<Customer, NormalisedCustomerEntity> customerMigrationProcessor,
            ItemWriter<NormalisedCustomerEntity> repositoryItemWriterForNormalisedCustomer) {

        return new StepBuilder(jobRepository)
                .<Customer, NormalisedCustomerEntity>chunk(10)
                .reader(customerJdbcCursorItemReader)
//                .reader(customerRepositoryItemReader)
                .processor(customerMigrationProcessor)
                .writer(repositoryItemWriterForNormalisedCustomer)
                .build();
    }

}
