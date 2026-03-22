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
    @Mapping(target = "accountId", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "bsb", expression = "java(purgeDashesAndSpaces(accountRequest.bsb()))")
    @Mapping(target = "accountNumber", expression = "java(purgeDashesAndSpaces(accountRequest.accountNumber()))")
    Account toEntity(AccountRequest accountRequest);

    AccountResponse toResponse(Account account);

    default Account updateAccountFromAccountRequest(Account account, AccountRequest accountRequest) {

        if (accountRequest == null) {
            return account;
        }

        if (accountRequest.bsb() != null) {
            account.setBsb(purgeDashesAndSpaces(accountRequest.bsb()));
        }
        if (accountRequest.accountNumber() != null) {
            account.setAccountNumber(purgeDashesAndSpaces(accountRequest.accountNumber()));
        }
        if (accountRequest.accountName() != null) {
            account.setAccountName(accountRequest.accountName());
        }
        if (accountRequest.type() != null) {
            account.setType(AccountType.valueOf(accountRequest.type()));
        }

        return account;
    }

    default void mutateAccountDetailsFromAccountRequest(Account account, AccountRequest replacement) {
        account.setBsb(replacement.bsb());
        account.setAccountNumber(replacement.accountNumber());
        account.setAccountName(replacement.accountName());
        account.setType(AccountType.valueOf(replacement.type()));
    }

    default String purgeDashesAndSpaces(String input) {
        return input.replaceAll("-", "").replaceAll("\\s", "");
    }
}
