package dev.aj.billing_service.controller;

import dev.aj.billing_service.service.BillingService;
import dev.aj.order_service.model.invoice.Invoice;
import dev.aj.order_service.model.invoice.InvoiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        value = "/api/v1/billings",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    @PostMapping(path = "/")
    public ResponseEntity<Invoice> createInvoice(@RequestBody @Validated InvoiceRequest invoiceRequest) {

        return ResponseEntity
                .ok(billingService.createInvoice(invoiceRequest));
    }
}
