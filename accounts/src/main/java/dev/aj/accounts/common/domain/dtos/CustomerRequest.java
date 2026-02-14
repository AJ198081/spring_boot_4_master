package dev.aj.accounts.common.domain.dtos;

import lombok.Builder;

import java.util.Set;

@Builder
public record CustomerRequest(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        Set<AddressRequest> addresses
) {
}
