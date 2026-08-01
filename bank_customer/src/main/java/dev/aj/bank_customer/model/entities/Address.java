package dev.aj.bank_customer.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Size;

@Embeddable
public record Address(
        @Enumerated(EnumType.STRING)
        AddressType type,

        @Column(columnDefinition = "varchar(30)")
        @Size(max = 30, message = "Street number must not exceed 30 characters")
        String streetNumber,

        @Column(columnDefinition = "varchar(150)")
        @Size(max = 150, message = "Street must not exceed 150 characters")
        String street,

        @Column(columnDefinition = "varchar(50)")
        @Size(max = 50, message = "City must not exceed 50 characters")
        String city,

        @Column(columnDefinition = "varchar(50)")
        @Size(max = 50, message = "State must not exceed 50 characters")
        String state,

        @Column(columnDefinition = "varchar(10)")
        @Size(max = 10, message = "Post code must not exceed 10 characters")
        String postCode,

        @Column(columnDefinition = "varchar(50)")
        @Size(max = 50, message = "Country must not exceed 50 characters")
        String country
) {
    public enum AddressType {
        HOME,
        OFFICE,
        DELIVERY
    }
}
