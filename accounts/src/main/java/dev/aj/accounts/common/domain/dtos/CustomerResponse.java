package dev.aj.accounts.common.domain.dtos;

import org.jspecify.annotations.NonNull;

import java.util.Set;
import java.util.UUID;

public record CustomerResponse(
        @NonNull UUID customerId,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        Set<AddressResponse> addresses) {
}
