package dev.aj.bank_customer.migration.jobs;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MigrationJob {

    private final JobRepository jobRepository;
    private final Step etlStepForMigration;

    public Job customerMigrationJob() {

        return new JobBuilder("customerMigrationJob", jobRepository)
                .start(etlStepForMigration)
                .build();
    }

}
