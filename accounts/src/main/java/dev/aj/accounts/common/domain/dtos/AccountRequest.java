package dev.aj.accounts.common.domain.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "AccountRequest"
, description = "Request object to create a new account")
public record AccountRequest (
        @Schema(description = "The Bank State Branch code", example = "060-451")
        String bsb,
        @Schema(description = "The account number", example = "0457-896-512")
        String accountNumber,
        @Schema(description = "The name of the account holder", example = "Ralph Norris")
        String accountName,
        @Schema(description = "The type of account", example = "SAVINGS")
        String type
) { }
