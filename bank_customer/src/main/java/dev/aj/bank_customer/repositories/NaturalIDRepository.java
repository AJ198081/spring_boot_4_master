package dev.aj.bank_customer.repositories;

import org.hibernate.annotations.NaturalId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface NaturalIDRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

//  If entity has a single @NaturalId field
    @NaturalId
    Optional<T> findByExternalId(UUID externalId);

//  If entity has multiple @NaturalId fields
    @NaturalId
    Optional<T> findByNaturalIdMap(Map<String, Object> naturalIdMap);

}
