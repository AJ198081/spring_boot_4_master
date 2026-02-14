package dev.aj.accounts.account.controllers;

import dev.aj.accounts.account.services.AccountService;
import dev.aj.accounts.common.domain.dtos.AccountRequest;
import dev.aj.accounts.common.domain.dtos.AccountResponse;
import dev.aj.accounts.common.exceptions.AccountAlreadyExistsException;
import dev.aj.accounts.common.exceptions.AccountNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/")
    public ResponseEntity<HttpStatus> createAccount(@Validated @RequestBody AccountRequest creationRequest) throws AccountAlreadyExistsException {

        AccountResponse response = accountService.createAccount(creationRequest);

        return ResponseEntity
                .created(UriComponentsBuilder.fromPath("/{accountId}")
                        .buildAndExpand(response.accountId().toString())
                        .toUri())
                .build();
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccount(@NonNull @PathVariable UUID accountId) throws AccountNotFoundException {
        return ResponseEntity.ok(accountService.getAccountById(accountId));
    }


}
