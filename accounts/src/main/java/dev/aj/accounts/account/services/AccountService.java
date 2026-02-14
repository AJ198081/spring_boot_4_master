package dev.aj.accounts.account.services;

import dev.aj.accounts.common.domain.dtos.AccountRequest;
import dev.aj.accounts.common.domain.dtos.AccountResponse;
import dev.aj.accounts.common.exceptions.AccountAlreadyExistsException;
import dev.aj.accounts.common.exceptions.AccountNotFoundException;

import java.util.UUID;

public interface AccountService {
    AccountResponse createAccount(AccountRequest request) throws AccountAlreadyExistsException;

    AccountResponse getAccountById(UUID accountId) throws AccountNotFoundException;
}
