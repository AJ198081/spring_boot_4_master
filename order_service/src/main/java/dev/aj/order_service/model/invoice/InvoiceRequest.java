package dev.aj.order_service.model.invoice;

import dev.aj.order_service.model.common.ABN;
import dev.aj.order_service.model.common.PriceSummary;

import java.util.UUID;

public sealed interface InvoiceRequest {

    record Paid(UUID orderId, UUID customerId, UUID transactionId, PriceSummary priceSummary) implements InvoiceRequest {
    }

    record Unpaid(UUID orderId, UUID customerId, String businessName, ABN abn, PriceSummary priceSummary) implements InvoiceRequest {
    }
}
