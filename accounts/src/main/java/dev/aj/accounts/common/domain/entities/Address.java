package dev.aj.accounts.common.domain.entities;

import dev.aj.accounts.common.domain.entities.enums.AddressType;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "address")
@EntityListeners(AuditingEntityListener.class)
public class Address {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_seq")
    @SequenceGenerator(name = "address_seq")
    @Column(name = "id", nullable = false)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "address_type", columnDefinition = "VARCHAR(20)", nullable = false)
    @Builder.Default
    private AddressType addressType = AddressType.RESIDENTIAL;

    @Column(name = "address_line_1", columnDefinition = "VARCHAR(300)", nullable = false)
    private String addressLine1;
    
    @Column(name = "address_line_2", columnDefinition = "VARCHAR(300)")
    private String addressLine2;

    @Column(name = "city", columnDefinition = "VARCHAR(100)", nullable = false)
    private String city;

    @Column(name = "state", columnDefinition = "VARCHAR(50)", nullable = false)
    private String state;

    @Column(name = "post_code", columnDefinition = "VARCHAR(10)", nullable = false)
    private String postCode;

    @Column(name = "country", columnDefinition = "VARCHAR(100)", nullable = false)
    private String country;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Builder.Default
    private MetaData metaData = new MetaData();

    public void setCustomer(Customer customer) {
        this.customer = customer;
        customer.getAddresses().add(this);
    }
}