package dev.aj.order_service.service;

import dev.aj.order_service.model.order.Order;
import dev.aj.order_service.model.shipping.ShipmentResponse;

public interface ShippingService {

    ShipmentResponse createShipment(Order order);

}
