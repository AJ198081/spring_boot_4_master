package dev.aj.order_service.client;

import dev.aj.order_service.model.shipping.Shipment;
import dev.aj.order_service.model.shipping.ShipmentStatus;

import java.util.UUID;

public interface ShippingClient {

    ShipmentStatus createShipment(Shipment shipment);

    boolean checkIfOrderCanBeCancelled(UUID orderId);
}
