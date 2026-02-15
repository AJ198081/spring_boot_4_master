package dev.aj.accounts.common.domain.dtos;

import dev.aj.accounts.common.domain.entities.enums.AddressType;
import lombok.Builder;

@Builder
public record AddressRequest(
        String addressLine1,
        String addressLine2,
        String city,
        String state,
        String postCode,
        String country,
        AddressType addressType
) {
}
