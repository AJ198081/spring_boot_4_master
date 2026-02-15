package dev.aj.accounts.common.domain.dtos;

import dev.aj.accounts.common.domain.entities.enums.AddressType;

public record AddressResponse(
        AddressType addressType,
        String addressLine1,
        String addressLine2,
        String city,
        String state,
        String postCode,
        String country
) {
}
