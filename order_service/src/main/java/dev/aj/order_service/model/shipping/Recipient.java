package dev.aj.order_service.model.shipping;

import dev.aj.order_service.model.common.Address;

public record Recipient(String name, Address address) {
}
