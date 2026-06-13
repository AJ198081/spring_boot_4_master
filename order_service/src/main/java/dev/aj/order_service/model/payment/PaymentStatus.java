package dev.aj.order_service.model.payment;

import java.util.UUID;

public sealed interface PaymentStatus {

    record Pending(PaymentRequest paymentRequest) implements PaymentStatus {
    }
    record Completed(PaymentRequest paymentRequest, UUID transactionId) implements PaymentStatus {
    }
    record Failed(PaymentRequest paymentRequest, String reason) implements PaymentStatus {
    }

}
