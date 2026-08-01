package dev.aj.bank_customer.migration.model.entities;

import dev.aj.bank_customer.model.entities.Address;
import dev.aj.bank_customer.model.entities.AuditMetaData;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "normalised_addresses")
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("all")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_entity_gen")
    @SequenceGenerator(name = "address_entity_gen", sequenceName = "address_entity_seq")
    @Column(nullable = false)
    @JdbcTypeCode(SqlTypes.BIGINT)
    private Long id;

    @EqualsAndHashCode.Include
    @Embedded
    private Address address;

    @ManyToOne
    @JoinColumn(name = "normalised_customer_entity_id", referencedColumnName = "id")
    private NormalisedCustomerEntity normalisedCustomerEntity;

    @Embedded
    private AuditMetaData auditMetaData = new AuditMetaData();

    public void setNormalisedCustomerEntity(NormalisedCustomerEntity normalisedCustomerEntity) {
        this.normalisedCustomerEntity = normalisedCustomerEntity;

        normalisedCustomerEntity.getAddressEntities().add(this);
    }

}
