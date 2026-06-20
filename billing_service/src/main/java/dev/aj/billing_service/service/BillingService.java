package dev.aj.billing_service.service;

import dev.aj.order_service.model.invoice.Invoice;
import dev.aj.order_service.model.invoice.InvoiceRequest;

public interface BillingService {
    Invoice createInvoice(InvoiceRequest invoiceRequest);
}
