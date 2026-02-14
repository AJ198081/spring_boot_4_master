package dev.aj.accounts.common.domain.dtos;

import java.util.Set;
import java.util.UUID;

public record CustomerResponse(
        UUID customerId,
        String firstName,
        String lastName,
        Set<AddressResponse> addresses) {
}
