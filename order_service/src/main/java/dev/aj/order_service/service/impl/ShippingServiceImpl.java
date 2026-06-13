package dev.aj.order_service.service.impl;

import dev.aj.order_service.client.ShippingClient;
import dev.aj.order_service.model.order.Order;
import dev.aj.order_service.model.shipping.Recipient;
import dev.aj.order_service.model.shipping.Shipment;
import dev.aj.order_service.model.shipping.ShipmentItem;
import dev.aj.order_service.model.shipping.ShipmentResponse;
import dev.aj.order_service.service.ShippingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@NullMarked
public class ShippingServiceImpl implements ShippingService {

    private final ShippingClient shippingClient;

    @Override
    public ShipmentResponse createShipment(Order order) {
        log.info("Creating a shipment for order {}", order);

        return shippingClient.createShipment(
                new Shipment(
                        order.orderId(),
                        new Recipient(order.customer().name(), order.customer().address()),
                        order.items().stream()
                                .map(orderItem ->
                                        new ShipmentItem(orderItem.product().productId(),
                                                orderItem.product().name(),
                                                orderItem.quantity()))
                                .toList()
                ));
    }
}
