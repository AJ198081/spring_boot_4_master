package dev.aj.bank_customer.model.dtos;

import dev.aj.bank_commons.types.Email;

public record CustomerRequest(
        String firstName,
        String lastName,
        Email email,
        AddressRequest address,
        String phoneNumber
) {
}
