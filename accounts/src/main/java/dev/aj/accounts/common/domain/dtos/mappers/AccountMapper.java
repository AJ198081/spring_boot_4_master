package dev.aj.accounts.common.domain.dtos.mappers;

import dev.aj.accounts.common.domain.dtos.AccountRequest;
import dev.aj.accounts.common.domain.dtos.AccountResponse;
import dev.aj.accounts.common.domain.entities.Account;
import dev.aj.accounts.common.domain.entities.enums.AccountType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface AccountMapper {

    @Mapping(target = "metaData", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "accountId", ignore = true)
    Account toEntity(AccountRequest accountRequest);

    AccountResponse toResponse(Account account);

    default Account updateAccountFromAccountRequest(Account account, AccountRequest accountRequest) {

        if (accountRequest == null) {
            return account;
        }

        if (accountRequest.bsb() != null) {
            account.setBsb(accountRequest.bsb());
        }
        if (accountRequest.accountNumber() != null) {
            account.setAccountNumber(accountRequest.accountNumber());
        }
        if (accountRequest.accountName() != null) {
            account.setAccountName(accountRequest.accountName());
        }
        if (accountRequest.accountType() != null) {
            account.setAccountType(AccountType.valueOf(accountRequest.accountType()));
        }

        return account;
    }

    default void mutateAccountDetailsFromAccountRequest(Account account, AccountRequest replacement) {
        account.setBsb(replacement.bsb());
        account.setAccountNumber(replacement.accountNumber());
        account.setAccountName(replacement.accountName());
        account.setAccountType(AccountType.valueOf(replacement.accountType()));
    }
}
