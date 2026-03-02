package dev.aj.accounts.account.services;

import dev.aj.accounts.common.domain.dtos.AccountRequest;
import dev.aj.accounts.common.domain.dtos.AccountResponse;
import dev.aj.accounts.common.exceptions.AccountAlreadyExistsException;
import dev.aj.accounts.common.exceptions.AccountNotFoundException;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public interface AccountService {
    AccountResponse createAccount(AccountRequest request) throws AccountAlreadyExistsException;

    AccountResponse getAccountById(UUID accountId) throws AccountNotFoundException;

    void updateAccount(@NonNull UUID accountId, @Valid AccountRequest updateRequest) throws AccountNotFoundException;

    void replaceAccount(@NonNull UUID accountId, @Valid AccountRequest replacement) throws AccountNotFoundException;
}
