package dev.aj.order_service.model.shipping;

import java.util.UUID;

public record ShipmentResponse(UUID shipmentId, Shipment shipment, TrackingDetails trackingDetails) {
}
