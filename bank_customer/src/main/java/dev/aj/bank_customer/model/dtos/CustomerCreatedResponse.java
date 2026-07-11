package dev.aj.bank_customer.model.dtos;

import org.jspecify.annotations.NullMarked;

import java.time.ZonedDateTime;
import java.util.UUID;

@NullMarked
public record CustomerCreatedResponse(
        UUID externalId,
        KycStatus kycStatus,
        int version,
        ZonedDateTime createdAt
        ) {
    public enum KycStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
}
