package dev.aj.bank_customer.migration.readers;

import dev.aj.bank_customer.model.entities.Address;
import dev.aj.bank_customer.model.entities.AuditMetaData;
import dev.aj.bank_customer.model.entities.Customer;
import dev.aj.bank_customer.model.entities.KycStatus;
import dev.aj.bank_customer.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.data.RepositoryItemReader;
import org.springframework.batch.infrastructure.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.infrastructure.item.database.JdbcCursorItemReader;
import org.springframework.batch.infrastructure.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import javax.sql.DataSource;
import java.time.ZoneId;
import java.util.Map;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class CustomerReader {

    private final DataSource dataSource;

    @Bean
    public JdbcCursorItemReader<Customer> customerJdbcCursorItemReader() {

        return new JdbcCursorItemReaderBuilder<Customer>()
                .name("customerJdbcCursorItemReader")
                .dataSource(dataSource)
                .sql("select * from customers")
                .rowMapper((rs, _) -> Customer.builder()
                        .id(rs.getLong("id"))
                        .externalId(rs.getObject("external_id", UUID.class))
                        .firstName(rs.getString("first_name"))
                        .lastName(rs.getString("last_name"))
                        .email(rs.getString("email"))
                        .phone(rs.getString("phone"))
                        .dateOfBirth(rs.getDate("date_of_birth") != null ? rs.getDate("date_of_birth").toLocalDate() : null)
                        .kycStatus(KycStatus.valueOf(rs.getString("kyc_status")))
                        .requestFingerPrint(rs.getString("request_finger_print"))
                        .address(new Address(
                                Address.AddressType.valueOf(rs.getString("type")),
                                rs.getString("street_number"),
                                rs.getString("street"),
                                rs.getString("city"),
                                rs.getString("state"),
                                rs.getString("post_code"),
                                rs.getString("country")))
                        .auditMetaData(AuditMetaData.builder()
                                .createdDate(rs.getTimestamp("created_date") != null
                                        ? rs.getTimestamp("created_date").toInstant().atZone(ZoneId.systemDefault())
                                        : null)
                                .createdBy(rs.getString("created_by") != null
                                        ? rs.getString("created_by")
                                        : null)
                                .lastModifiedDate(rs.getTimestamp("last_modified_date") != null
                                        ? rs.getTimestamp("last_modified_date").toInstant().atZone(ZoneId.systemDefault())
                                        : null)
                                .lastModifiedBy(rs.getString("last_modified_by") != null
                                        ? rs.getString("last_modified_by")
                                        : null).build())
                        .build())
                .build();
    }

    @Bean
    public RepositoryItemReader<Customer> customerRepositoryItemReader(
            CustomerRepository customerRepository
    ) {
        return new RepositoryItemReaderBuilder<Customer>()
                .name("customerRepositoryItemReader")
                .repository(customerRepository)
                .methodName("findAll")
                .sorts(Map.of("id", Sort.Direction.ASC))
                .pageSize(10)
                .build();
    }
}