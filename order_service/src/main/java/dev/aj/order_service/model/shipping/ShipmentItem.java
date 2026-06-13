package dev.aj.order_service.model.shipping;

import java.util.UUID;

public record ShipmentItem(UUID productId, int quantity) {
}
