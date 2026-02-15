package dev.aj.accounts.customer.services.repositories;


import dev.aj.accounts.common.domain.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomersRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findCustomerByCustomerId(UUID customerId);
    boolean existsCustomerByEmail(String email);

    @Transactional
    @Modifying
    @Query("delete from Customer c where c.customerId = ?1")
    void deleteCustomerByCustomerId(UUID customerId);

}
