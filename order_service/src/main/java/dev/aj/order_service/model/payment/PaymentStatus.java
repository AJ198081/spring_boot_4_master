package dev.aj.order_service.model.payment;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "status")
public sealed interface PaymentStatus {

    PaymentRequest paymentRequest();

    record Pending(PaymentRequest paymentRequest) implements PaymentStatus {
    }
    record Completed(PaymentRequest paymentRequest, UUID transactionId) implements PaymentStatus {
    }
    record Failed(PaymentRequest paymentRequest, String reason) implements PaymentStatus {
    }

}
