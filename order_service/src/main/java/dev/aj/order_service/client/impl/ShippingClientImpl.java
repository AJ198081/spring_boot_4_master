package dev.aj.order_service.client.impl;

import dev.aj.order_service.client.AbstractServiceClient;
import dev.aj.order_service.client.ShippingClient;
import dev.aj.order_service.model.shipping.Shipment;
import dev.aj.order_service.model.shipping.ShipmentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShippingClientImpl extends AbstractServiceClient implements ShippingClient {

    private final RestClient shippingClient;

    @Override
    public ShipmentStatus createShipment(Shipment shipment) {
        return executeRequest(() -> shippingClient.post()
                .uri("/")
                .body(shipment)
                .retrieve()
                .body(ShipmentStatus.Scheduled.class)
        );
    }

    @Override
    public boolean checkIfOrderCanBeCancelled(UUID orderId) {
        return Boolean.TRUE.equals(executeRequest(() -> shippingClient.get()
                .uri("/shipping/status/{orderId}", orderId))
                .retrieve()
                .body(Boolean.class));
    }

    @Override
    public String getServiceName() {
        return "shipping-service";
    }
}
