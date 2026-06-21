package dev.aj.shipping_service.service.impl;

import dev.aj.order_service.model.common.Email;
import dev.aj.order_service.model.common.Mobile;
import dev.aj.order_service.model.shipping.Shipment;
import dev.aj.order_service.model.shipping.ShipmentStatus;
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

    private static final ConcurrentHashMap<Shipment, ShipmentStatus> SHIPMENTS = new ConcurrentHashMap<>();

    private final Faker faker;

    @Override
    public ShipmentStatus createShipment(Shipment shipment) {

        return SHIPMENTS.computeIfAbsent(shipment, newShipment -> {
            String courierCompanyName = faker.company().name();
            return new ShipmentStatus.Scheduled(
                    newShipment,
                    new TrackingDetails(
                            new TrackingDetails.Carrier(
                                    courierCompanyName,
                                    new Email(faker.internet().emailAddress(courierCompanyName)),
                                    new Mobile(faker.phoneNumber().cellPhone().replaceAll("\\s", ""))),
                            UUID.randomUUID().toString(),
                            LocalDate.now().plusDays(faker.number().numberBetween(5, 7))
                    ),
                    LocalDate.now()
            );
        });
    }

    @Override
    public boolean checkIfOrderCanBeCancelled(UUID orderId) {
        return SHIPMENTS.values().stream()
                .filter(shipmentStatus -> shipmentStatus.shipment().orderId().equals(orderId))
                .filter(ShipmentStatus.Scheduled.class::isInstance)
                .map(ShipmentStatus.Scheduled.class::cast)
                .anyMatch(shipmentStatus -> shipmentStatus.trackingDetails().estimatedDeliveryDate()
                        .isBefore(LocalDate.now().minusDays(1)));
    }
}
