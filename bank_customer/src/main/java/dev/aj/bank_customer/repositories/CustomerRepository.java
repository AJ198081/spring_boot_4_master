package dev.aj.bank_customer.repositories;

import dev.aj.bank_customer.model.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@SuppressWarnings("unused")
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByExternalId(UUID customerExternalId);
    Optional<Customer> findByEmail(String email);
    boolean existsByEmail(String email);

    Optional<Customer> findByRequestFingerPrint(String requestFingerPrint);
}
