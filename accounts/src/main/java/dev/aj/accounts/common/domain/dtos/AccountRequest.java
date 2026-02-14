package dev.aj.accounts.common.domain.dtos;

import lombok.Builder;

@Builder
public record AccountRequest (
        String bsb,
        String accountNumber,
        String accountName,
        String accountType
) {

}
