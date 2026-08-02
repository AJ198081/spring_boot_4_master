package dev.aj.bank_customer.migration.writers;

import dev.aj.bank_customer.migration.model.dtos.NormalisedCustomer;
import dev.aj.bank_customer.migration.model.entities.AddressEntity;
import dev.aj.bank_customer.migration.model.entities.NormalisedCustomerEntity;
import dev.aj.bank_customer.migration.model.entities.NormalisedCustomerRecordEntity;
import dev.aj.bank_customer.migration.repositories.AddressEntityRepository;
import dev.aj.bank_customer.migration.repositories.NormalisedCustomerEntityRepository;
import dev.aj.bank_customer.migration.repositories.NormalisedCustomerRecordEntityRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.data.RepositoryItemWriter;
import org.springframework.batch.infrastructure.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NormalisedEntityWriter {

    @Bean
    public JpaItemWriter<NormalisedCustomer> jpaItemWriterForNormalisedCustomer(EntityManagerFactory entityManagerFactory) {
        return new JpaItemWriterBuilder<NormalisedCustomer>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    @Bean
    public JpaItemWriter<AddressEntity> jpaItemWriterForAddressEntity(EntityManagerFactory entityManagerFactory) {
        return new JpaItemWriterBuilder<AddressEntity>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    @Bean
    public RepositoryItemWriter<NormalisedCustomerEntity> repositoryItemWriterForNormalisedCustomer(NormalisedCustomerEntityRepository customerEntityRepository) {
        return new RepositoryItemWriterBuilder<NormalisedCustomerEntity>()
                .repository(customerEntityRepository)
                .build();
    }

    @Bean
    public RepositoryItemWriter<AddressEntity> repositoryItemWriterForAddressEntity(AddressEntityRepository addressEntityRepository) {
        return new RepositoryItemWriterBuilder<AddressEntity>()
                .repository(addressEntityRepository)
                .build();
    }

    @Bean
    public RepositoryItemWriter<NormalisedCustomerRecordEntity> repositoryItemWriterForNormalisedCustomerRecordEntity(NormalisedCustomerRecordEntityRepository normalisedCustomerRecordEntityRepository) {
        return new RepositoryItemWriterBuilder<NormalisedCustomerRecordEntity>()
                .repository(normalisedCustomerRecordEntityRepository)
                .build();
    }

    @Bean
    public ItemWriter<NormalisedCustomerRecordEntity> jdbcBatchItemWriterForNormalisedCustomerRecordEntity(JdbcAggregateTemplate jdbcAggregateTemplate) {

        return chunk -> jdbcAggregateTemplate.insertAll(chunk.getItems());
    }

}
