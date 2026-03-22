package dev.aj.accounts.account.controllers;

import dev.aj.accounts.account.services.AccountService;
import dev.aj.accounts.common.domain.dtos.AccountRequest;
import dev.aj.accounts.common.domain.dtos.AccountResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping(
        path = "${api.basePath.accounts}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
@Validated
@Tag(name = "Account API", description = "CRUD operations for Accounts entity")
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "Create a new Account")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Account created successfully",
                    content = @Content(schema = @Schema(implementation = ResponseEntity.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid account request parameters passed",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Account already exists",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PostMapping("/")
    public ResponseEntity<HttpStatus> createAccount(@Valid @RequestBody AccountRequest creationRequest) {

        AccountResponse response = accountService.createAccount(creationRequest);

        return ResponseEntity
                .created(UriComponentsBuilder.fromPath("/{accountId}")
                        .buildAndExpand(response.accountId().toString())
                        .toUri())
                .build();
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccount(@NonNull @PathVariable UUID accountId) {
        return ResponseEntity.ok(accountService.getAccountById(accountId));
    }

    @PutMapping("/{accountId}")
    @ApiResponse(content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    public ResponseEntity<HttpStatus> replaceAccount(@NonNull @PathVariable UUID accountId, @Valid @RequestBody AccountRequest replacement) {
        accountService.replaceAccount(accountId, replacement);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{accountId}")
    @ApiResponse(content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    public ResponseEntity<HttpStatus> updateAccount(@NonNull @PathVariable UUID accountId, @Valid @RequestBody AccountRequest updateRequest) {
        accountService.updateAccount(accountId, updateRequest);
        return ResponseEntity.noContent().build();
    }


}
