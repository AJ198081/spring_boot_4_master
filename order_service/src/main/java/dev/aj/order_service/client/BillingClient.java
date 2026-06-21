package dev.aj.order_service.client;

import dev.aj.order_service.model.invoice.Invoice;
import dev.aj.order_service.model.invoice.InvoiceRequest;

import java.util.UUID;

public interface BillingClient {
    Invoice createInvoice(InvoiceRequest invoiceRequest);
    Invoice cancelInvoice(UUID invoiceId);
}
