package dev.aj.bank_customer.migration;

import dev.aj.bank_customer.migration.jobs.MigrationJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.parameters.JobParameter;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerMigration {

    private final JobOperator jobOperator;
    private final MigrationJob migrationJob;

    public void runMigration(JobParameters jobParameters) {

        if (jobParameters == null) {
            jobParameters = new JobParameters(
                    Set.of(new JobParameter<>("migration", LocalDateTime.now(), LocalDateTime.class)));
        }

        try {
            jobOperator.start(migrationJob.customerMigrationJob(), jobParameters);
        } catch (Exception e) {
            log.error("Error running migration", e);
        }
    }

    public void runTransfer(JobParameters jobParameters) {

        if (jobParameters == null) {
            jobParameters = new JobParameters(
                    Set.of(new JobParameter<>("transfer", LocalDateTime.now(), LocalDateTime.class)));
        }

        try {
            jobOperator.start(migrationJob.customerTransferJob(), jobParameters);
        } catch (Exception e) {
            log.error("Error running transfer", e);
        }

    }

}
