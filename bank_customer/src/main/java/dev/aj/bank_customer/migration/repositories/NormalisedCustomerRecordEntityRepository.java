package dev.aj.bank_customer.migration.repositories;

import dev.aj.bank_customer.migration.model.entities.NormalisedCustomerRecordEntity;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NormalisedCustomerRecordEntityRepository extends ListCrudRepository<NormalisedCustomerRecordEntity, Long> {


}
