package dev.aj.order_service.model.shipping;

import dev.aj.order_service.model.common.Email;
import dev.aj.order_service.model.common.Mobile;

import java.time.LocalDate;

public record TrackingDetails(Carrier carrier, String trackingNumber, LocalDate estimatedDeliveryDate) {
    public record Carrier(String name, Email email, Mobile mobile) {
    }

}


