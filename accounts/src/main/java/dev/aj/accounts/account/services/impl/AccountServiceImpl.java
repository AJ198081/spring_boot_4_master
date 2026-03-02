package dev.aj.accounts.account.services.impl;

import dev.aj.accounts.account.services.AccountService;
import dev.aj.accounts.account.services.repositories.AccountsRepository;
import dev.aj.accounts.common.domain.dtos.AccountRequest;
import dev.aj.accounts.common.domain.dtos.AccountResponse;
import dev.aj.accounts.common.domain.dtos.mappers.AccountMapper;
import dev.aj.accounts.common.domain.entities.Account;
import dev.aj.accounts.common.exceptions.AccountAlreadyExistsException;
import dev.aj.accounts.common.exceptions.AccountNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountsRepository accountsRepository;
    private final AccountMapper accountMapper;

    @Override
    public AccountResponse createAccount(AccountRequest request) throws AccountAlreadyExistsException {

        if (accountsRepository.existsAccountByBsbAndAccountNumber(request.bsb(), request.accountNumber())) {
            throw new AccountAlreadyExistsException("Account with BSB %s and account number %s already exist."
                    .formatted(request.bsb(), request.accountNumber())
            );
        }

        Account newAccount = accountMapper.toEntity(request);

        Account createdAccount = accountsRepository.save(newAccount);

        return accountMapper.toResponse(createdAccount);
    }

    @Override
    public AccountResponse getAccountById(UUID accountId) throws AccountNotFoundException {
        return accountsRepository.findByAccountId(accountId)
                .map(accountMapper::toResponse)
                .orElseThrow(() -> new AccountNotFoundException("Account id %s doesn't exist.".formatted(accountId)));
    }

    @Override
    public void updateAccount(@NonNull UUID accountId, AccountRequest updateRequest) throws AccountNotFoundException {
        accountsRepository.findByAccountId(accountId)
                .map(account -> accountMapper.updateAccountFromAccountRequest(account, updateRequest))
                .map(accountsRepository::save)
                .orElseThrow(() -> new AccountNotFoundException("Account id %s doesn't exist.".formatted(accountId)));
    }

    @Override
    public void replaceAccount(@NonNull UUID accountId, AccountRequest replacement) throws AccountNotFoundException {
        Account modifiedAccount = accountsRepository.findByAccountId(accountId)
                .map(account -> {
                    accountMapper.mutateAccountDetailsFromAccountRequest(account, replacement);
                    return accountsRepository.save(account);
                })
                .orElseThrow(() -> new AccountNotFoundException("Account id %s doesn't exist.".formatted(accountId)));
        log.info("Account ID: {} replaced with {}", accountId, modifiedAccount);
    }
}
