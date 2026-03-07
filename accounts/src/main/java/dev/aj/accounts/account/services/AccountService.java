package dev.aj.accounts.account.services;

import dev.aj.accounts.common.domain.dtos.AccountRequest;
import dev.aj.accounts.common.domain.dtos.AccountResponse;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public interface AccountService {
    AccountResponse createAccount(AccountRequest request);

    AccountResponse getAccountById(UUID accountId);

    void updateAccount(@NonNull UUID accountId, @Valid AccountRequest updateRequest);

    void replaceAccount(@NonNull UUID accountId, @Valid AccountRequest replacement);
}
