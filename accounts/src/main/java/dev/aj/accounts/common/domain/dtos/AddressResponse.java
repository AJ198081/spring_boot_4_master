package dev.aj.accounts.common.domain.dtos;

public record AddressResponse(
        String addressLine1,
        String addressLine2,
        String city,
        String state,
        String postCode,
        String country
) {
}
