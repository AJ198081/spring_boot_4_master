package dev.aj.shipping_service.controller;

import dev.aj.order_service.model.shipping.Shipment;
import dev.aj.order_service.model.shipping.ShipmentStatus;
import dev.aj.shipping_service.service.ShippingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "${shipping_service.uri}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ShippingController {

    private final ShippingService shippingService;

    @PostMapping(path = "/")
    public ResponseEntity<ShipmentStatus> createShipment(@RequestBody @Validated Shipment shipment) {
        return ResponseEntity.ok(shippingService.createShipment(shipment));
    }


}
