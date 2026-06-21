package dev.aj.order_service.service;

import dev.aj.order_service.model.order.Order;
import dev.aj.order_service.model.shipping.ShipmentStatus;

import java.util.UUID;

public interface ShippingService {

    ShipmentStatus createShipment(Order order);

    boolean canOrderBeCancelled(UUID orderId);
}
