package dev.aj.order_service.client;

import dev.aj.order_service.model.shipping.Shipment;
import dev.aj.order_service.model.shipping.ShipmentResponse;

public interface ShippingClient {

    ShipmentResponse createShipment(Shipment shipment);
}
