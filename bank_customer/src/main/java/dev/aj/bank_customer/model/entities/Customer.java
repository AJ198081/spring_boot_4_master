package dev.aj.bank_customer.model.entities;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.UUID;

/***
 * READ_ONLY: Used only for entities that never change (an exception is thrown if an attempt to update such an entity is made).
 *      It’s very simple and performative. It’s suitable for static reference data that doesn’t change.
 * NONSTRICT_READ_WRITE: Cache is updated after the transaction that changed the affected data has been committed.
 *      Thus, strong consistency isn’t guaranteed, and there’s a small time window in which stale data may be obtained from the cache.
 *      This kind of strategy is suitable for use cases that can tolerate eventual consistency.
 * READ_WRITE: This strategy guarantees strong consistency, which it achieves by using ‘soft’ locks.
 *      When a cached entity is updated, a soft lock is stored in the cache for that entity as well, which is released after the transaction is committed.
 *      All concurrent transactions that access soft-locked entries will fetch the corresponding data directly from the database.
 * TRANSACTIONAL: Cache changes are done in distributed XA transactions.
 *      A change in a cached entity is either committed or rolled back in both the database and cache in the same XA transaction.
 */

@Entity
@Table(name = "customers",
        uniqueConstraints = {
                @UniqueConstraint(name = "uniq_customer_external_id", columnNames = "external_id"),
                @UniqueConstraint(name = "uniq_customer_email", columnNames = "email")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@NaturalIdCache
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customers_gen")
    @SequenceGenerator(name = "customers_gen", sequenceName = "customers_seq")
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.BIGINT)
    private Long id;

    @NaturalId
    @Column(name = "external_id", nullable = false, columnDefinition = "UUID", updatable = false)
    private UUID externalId;

    @Version
    @Column(nullable = false)
    private short version;

    @Column(name = "first_name", nullable = false, columnDefinition = "VARCHAR(100)")
    private String firstName;

    @Column(name = "last_name", nullable = false, columnDefinition = "VARCHAR(150)")
    private String lastName;

    @Column(nullable = false, columnDefinition = "DATE")
    private LocalDate dateOfBirth;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String email;

    @Column(nullable = false, columnDefinition = "VARCHAR(20)")
    private String phone;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(30)")
    private KycStatus kycStatus;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String requestFingerPrint;

    @Column(nullable = false, columnDefinition = "BOOLEAN default false")
    private boolean active;

    @Embedded
    @Builder.Default
    private AuditMetaData auditMetaData = new AuditMetaData();
}
