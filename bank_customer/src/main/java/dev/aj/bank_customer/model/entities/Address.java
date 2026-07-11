package dev.aj.bank_customer.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {

    @Enumerated(EnumType.STRING)
    private AddressType type;

    @Column(columnDefinition = "varchar(30)")
    private String streetNumber;
    @Column(columnDefinition = "varchar(150)")
    private String street;
    @Column(columnDefinition = "varchar(50)")
    private String city;
    @Column(columnDefinition = "varchar(50)")
    private String state;
    @Column(columnDefinition = "varchar(10)")
    private String postCode;
    @Column(columnDefinition = "varchar(50)")
    private String country;

    public enum AddressType {
        HOME,
        OFFICE,
        DELIVERY
    }
}
