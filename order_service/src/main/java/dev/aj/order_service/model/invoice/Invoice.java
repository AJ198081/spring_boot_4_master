package dev.aj.order_service.model.invoice;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.aj.order_service.model.common.ABN;
import dev.aj.order_service.model.common.PriceSummary;

import java.time.LocalDate;
import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "status")
public sealed interface Invoice {

    record Paid(UUID invoiceId, UUID orderId, UUID customerId, UUID transactionId, PriceSummary priceSummary) implements Invoice {
    }

    //TODO: ABN is only available for business/wholesale customers
    record Unpaid(UUID orderId, UUID customerId, ABN abn, PriceSummary priceSummary, LocalDate dueDate) implements Invoice {
    }
}
