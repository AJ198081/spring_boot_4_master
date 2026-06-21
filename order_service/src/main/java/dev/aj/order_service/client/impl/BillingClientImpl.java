package dev.aj.order_service.client.impl;

import dev.aj.order_service.client.AbstractServiceClient;
import dev.aj.order_service.client.BillingClient;
import dev.aj.order_service.model.invoice.Invoice;
import dev.aj.order_service.model.invoice.InvoiceRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class BillingClientImpl extends AbstractServiceClient implements BillingClient {

    private final RestClient billingClient;

    @Override
    public Invoice createInvoice(InvoiceRequest invoiceRequest) {

        log.info("Making a request to {}", invoiceRequest.getClass().getSimpleName().toLowerCase());

        return executeRequest(() -> billingClient.post()
                .uri("/")
                .body(invoiceRequest)
                .retrieve()
                .body(Invoice.class));
    }

    @Override
    public String getServiceName() {
        return "billing-service";
    }
}
