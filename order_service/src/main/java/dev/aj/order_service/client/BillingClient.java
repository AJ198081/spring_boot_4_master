package dev.aj.order_service.client;

import dev.aj.order_service.model.invoice.Invoice;
import dev.aj.order_service.model.invoice.InvoiceRequest;

public interface BillingClient {
    Invoice createInvoice(InvoiceRequest invoiceRequest);
}
