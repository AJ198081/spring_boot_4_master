package dev.aj.bank_customer.model.dtos;

public record AddressDto(
        AddressType addressType,
        String streetNumber,
        String street,
        String city,
        String state,
        String postCode,
        String country
) {

    public enum AddressType {
        HOME,
        OFFICE,
        DELIVERY
    }
}
