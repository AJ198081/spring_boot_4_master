package dev.aj.bank_customer.model.dtos;

import java.time.ZonedDateTime;
import java.util.UUID;

public record CustomerResponse(
        UUID externalId,
        short version,
        String firstName,
        String lastName,
        String email,
        String phone,
        AddressDto address,
        KycStatus kycStatus,
        boolean active,
        ZonedDateTime createdAt
) { }
