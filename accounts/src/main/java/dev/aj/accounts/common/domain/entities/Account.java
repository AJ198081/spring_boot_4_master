package dev.aj.accounts.common.domain.entities;

import dev.aj.accounts.common.domain.entities.enums.AccountType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@Table(
        name = "accounts",
        uniqueConstraints = {
        @UniqueConstraint(name = "uk_bsb_account_number", columnNames = {"bsb", "account_number"})
})
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_gen")
    @SequenceGenerator(name = "account_gen", sequenceName = "account_seq")
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.BIGINT)
    private Long id;

    @Column(name = "account_id", columnDefinition = "uuid not null unique", nullable = false, updatable = false, unique = true)
    private UUID accountId;
    
    @Column(name = "bsb", columnDefinition = "VARCHAR(6)", nullable = false)
    @NotEmpty(message = "BSB is required")
    @Pattern(regexp = "\\d{6}", message = "BSB must be 6 digits, without spaces")
    private String bsb;
    
    @Column(name = "account_number", columnDefinition = "VARCHAR(10)", nullable = false)
    @NotEmpty(message = "Account number is required")
    @Pattern(regexp = "\\d{10}", message = "Account number must be 10 digits, without spaces")
    private String accountNumber;

    @Column(name = "account_name", columnDefinition = "VARCHAR(100)", nullable = false)
    @NotEmpty(message = "Account name is required")
    @Size(max = 100)
    private String accountName;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false, referencedColumnName = "id")
    private Customer customer;

    @Builder.Default
    private MetaData metaData = new MetaData();
}
