package dev.aj.accounts.common.domain.dtos;

import dev.aj.accounts.common.domain.entities.enums.AddressType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Request object to create a new Address")
public record AddressRequest(
        @Schema(description = "Address line 1", example = "123 Main St")
        String addressLine1,
        @Schema(description = "Address line 2", example = "Apt 123")
        String addressLine2,
        @Schema(description = "The city", example = "Sydney")
        String city,
        @Schema(description = "The state", example = "NSW")
        String state,
        @Schema(description = "The post-code", example = "2000")
        String postCode,
        @Schema(description = "The country", example = "Australia")
        String country,
        @Schema(description = "The address type", example = "RESIDENTIAL")
        AddressType addressType
) {
}
