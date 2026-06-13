package dev.aj.order_service.model.invoice;

import dev.aj.order_service.model.common.ABN;
import dev.aj.order_service.model.common.PriceSummary;

import java.time.LocalDate;
import java.util.UUID;

public sealed interface Invoice {

    record Paid(UUID invoiceId, UUID orderId, UUID customerId, UUID transactionId, PriceSummary priceSummary) implements Invoice {
    }

    record Unpaid(UUID orderId, UUID customerId, ABN abn, PriceSummary priceSummary, LocalDate dueDate) implements Invoice {
    }
}
