package dev.aj.accounts.customer.services.repositories;


import dev.aj.accounts.common.domain.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomersRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findCustomerByCustomerId(UUID customerId);
}
