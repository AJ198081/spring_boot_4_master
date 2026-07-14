package dev.aj.bank_customer.model.entities;

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
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.UUID;

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
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customers_gen")
    @SequenceGenerator(name = "customers_gen", sequenceName = "customers_seq")
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.BIGINT)
    private Long id;

    @Column(name = "external_id", nullable = false, columnDefinition = "UUID")
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
