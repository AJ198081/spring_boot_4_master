package dev.aj.bank_customer.events;

import dev.aj.bank_customer.model.dtos.CustomerRequest;

import java.util.UUID;

public record CustomerCreateEvent(CustomerRequest customerRequest, UUID withExternalId) {
}
