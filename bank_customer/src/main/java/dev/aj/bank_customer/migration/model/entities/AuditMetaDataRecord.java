package dev.aj.bank_customer.migration.model.entities;

import java.time.OffsetDateTime;

public record AuditMetaDataRecord(
        String createdBy,
        String lastModifiedBy,
        OffsetDateTime createdDate,
        OffsetDateTime lastModifiedDate
) {
}
