package dev.aj.order_service.model.shipping;

import java.util.UUID;

public record Shipment(UUID orderId, Recipient recipient, ShipmentItem... items) {
}
