package dev.aj.shipping_service.service;

import dev.aj.order_service.model.shipping.Shipment;
import dev.aj.order_service.model.shipping.ShipmentResponse;

import java.util.UUID;

public interface ShippingService {

    ShipmentResponse createShipment(Shipment shipment);
    boolean checkIfOrderCanBeCancelled(UUID orderId);
}
