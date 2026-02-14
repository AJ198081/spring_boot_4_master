package dev.aj.accounts.account.services.repositories;

import dev.aj.accounts.common.domain.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountsRepository extends JpaRepository<Account, Long> {

    boolean existsAccountByBsbAndAccountNumber(String bsb, String accountNumber);

    Optional<Account> findByAccountId(UUID accountId);

}
