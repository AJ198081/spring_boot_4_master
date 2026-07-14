package dev.aj.bank_customer.model.dtos;

import dev.aj.bank_commons.types.Email;

import java.util.Date;

public record CustomerRequest(
        String firstName,
        String lastName,
        Email email,
        Date dateOfBirth,
        AddressDto address,
        String phoneNumber
) {
}
