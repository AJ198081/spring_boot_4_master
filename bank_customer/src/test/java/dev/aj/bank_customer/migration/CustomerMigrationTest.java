package dev.aj.bank_customer.migration;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class CustomerMigrationTest {

    @Autowired
    private CustomerMigration migrationJob;

    @Test
    void runMigration() {

        JobParameters jobParameters = new JobParametersBuilder()
                .addJobParameter("migration", LocalDateTime.now(), LocalDateTime.class)
                .toJobParameters();

        migrationJob.runMigration(jobParameters);
    }

    @Test
    void runTransfer() {

        JobParameters jobParameters = new JobParametersBuilder()
                .addJobParameter("transfer", LocalDateTime.now(), LocalDateTime.class)
                .toJobParameters();

        migrationJob.runTransfer(jobParameters);
    }
}