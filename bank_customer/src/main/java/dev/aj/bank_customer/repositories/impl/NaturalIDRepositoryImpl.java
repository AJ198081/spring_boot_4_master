package dev.aj.bank_customer.repositories.impl;

import dev.aj.bank_customer.repositories.NaturalIDRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.KeyType;
import org.hibernate.Session;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.modulith.NamedInterface;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@NamedInterface
@Transactional(readOnly = true)
public class NaturalIDRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements NaturalIDRepository<T, ID> {

    private final EntityManager entityManager;

    public NaturalIDRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);

        this.entityManager = entityManager;
    }

    @Override
    public Optional<T> findByExternalId(UUID externalId) {
        Session hibernateSession = entityManager.unwrap(Session.class);

        return Optional.of(
                hibernateSession
                .find(getDomainClass(), externalId, KeyType.NATURAL)
        );
    }

    @Override
    public Optional<T> findByNaturalIdMap(Map<String, Object> naturalIdMap) {
        return Optional.of(entityManager.unwrap(Session.class)
                .find(getDomainClass(), naturalIdMap, KeyType.NATURAL));
    }
}
