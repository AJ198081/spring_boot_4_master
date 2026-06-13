package dev.aj.order_service.client;

import dev.aj.order_service.model.payment.PaymentStatus;

import java.util.UUID;

public interface PaymentClient {

    PaymentStatus getPaymentStatus(UUID paymentId);

}
