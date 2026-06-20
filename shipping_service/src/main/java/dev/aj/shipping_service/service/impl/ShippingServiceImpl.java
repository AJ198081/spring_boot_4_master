package dev.aj.shipping_service.service.impl;

import dev.aj.order_service.model.common.Email;
import dev.aj.order_service.model.common.Mobile;
import dev.aj.order_service.model.shipping.Shipment;
import dev.aj.order_service.model.shipping.ShipmentResponse;
import dev.aj.order_service.model.shipping.TrackingDetails;
import dev.aj.shipping_service.service.ShippingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingServiceImpl implements ShippingService {

    private static final ConcurrentHashMap<Shipment, ShipmentResponse> SHIPMENTS = new ConcurrentHashMap<>();

    private final Faker faker;

    @Override
    public ShipmentResponse createShipment(Shipment shipment) {

        return SHIPMENTS.computeIfAbsent(shipment, newShipment -> {
            String courierCompanyName = faker.company().name();
            return new ShipmentResponse(
                    UUID.randomUUID(),
                    newShipment,
                    new TrackingDetails(
                            new TrackingDetails.Carrier(
                                    courierCompanyName,
                                    new Email(faker.internet().emailAddress(courierCompanyName)),
                                    new Mobile(faker.phoneNumber().cellPhone().replaceAll("\\s", ""))),
                            UUID.randomUUID().toString(),
                            LocalDate.now().plusDays(faker.number().numberBetween(5, 7))
                    )
            );
        });
    }

    @Override
    public boolean checkIfOrderCanBeCancelled(UUID orderId) {
        return SHIPMENTS.values().stream()
                .filter(shipmentResponse -> shipmentResponse.shipment().orderId()
                        .equals(orderId))
                .anyMatch(shipmentResponse -> shipmentResponse.trackingDetails().estimatedDeliveryDate()
                        .isBefore(LocalDate.now().minusDays(1)));
    }
}
