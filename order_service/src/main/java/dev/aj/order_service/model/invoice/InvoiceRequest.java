package dev.aj.order_service.model.invoice;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.aj.order_service.model.common.PriceSummary;
import dev.aj.order_service.model.payment.PaymentStatus;

import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.SIMPLE_NAME, include = JsonTypeInfo.As.PROPERTY, property = "status")
public sealed interface InvoiceRequest {

    record Paid(UUID orderId, UUID customerId, UUID transactionId, PriceSummary priceSummary) implements InvoiceRequest {
    }

    record Unpaid(UUID orderId, UUID customerId, PriceSummary priceSummary) implements InvoiceRequest {
        public Unpaid(PaymentStatus.Pending pendingPaymentStatus, PriceSummary priceSummary) {
            this(pendingPaymentStatus.paymentRequest().orderId(), pendingPaymentStatus.paymentRequest().customerId(), priceSummary);
        }
    }

    record Failed(UUID orderId, UUID customerId, PriceSummary priceSummary) implements InvoiceRequest {
    }


}
