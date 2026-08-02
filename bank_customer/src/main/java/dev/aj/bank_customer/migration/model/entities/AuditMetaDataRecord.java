package dev.aj.bank_customer.migration.model.entities;

import java.time.ZonedDateTime;

public record AuditMetaDataRecord(
        String createdBy,
        String lastModifiedBy,
        ZonedDateTime createdDate,
        ZonedDateTime lastModifiedDate
) {
}
