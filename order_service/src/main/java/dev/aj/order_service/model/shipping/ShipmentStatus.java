package dev.aj.order_service.model.shipping;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.LocalDate;

@JsonTypeInfo(use = JsonTypeInfo.Id.SIMPLE_NAME, property = "status", include = JsonTypeInfo.As.PROPERTY)
public sealed interface ShipmentStatus {

    Shipment shipment();

    record Scheduled(Shipment shipment, TrackingDetails trackingDetails, LocalDate scheduledOn) implements ShipmentStatus {}
    record Delivered(Shipment shipment, LocalDate deliveredOn) implements ShipmentStatus {}
    record Cancelled(Shipment shipment, String reason, LocalDate cancelledOn) implements ShipmentStatus {}
    record Failed(Shipment shipment, String reason, Recourse recourse) implements ShipmentStatus {}
    record Declined(Shipment shipment, String reason) implements ShipmentStatus {}

   enum Recourse {
        RETRY, CANCEL, CONTACT_CUSTOMER, OTHER
    }
}
