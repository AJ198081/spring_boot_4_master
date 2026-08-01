package dev.aj.bank_customer.migration.repositories;

import dev.aj.bank_customer.migration.model.entities.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressEntityRepository extends JpaRepository<AddressEntity, Long> {
}
