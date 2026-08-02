package dev.aj.bank_customer.migration.repositories;

import dev.aj.bank_customer.migration.model.entities.NormalisedAddressRecordEntity;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NormalisedAddressRecordEntityRepository extends ListCrudRepository<NormalisedAddressRecordEntity, String> {
}
