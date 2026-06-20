package dev.aj.payment_service.service;

import dev.aj.order_service.model.payment.PaymentRequest;
import dev.aj.order_service.model.payment.PaymentStatus;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public interface PaymentService {

    PaymentStatus process(PaymentRequest paymentRequest);

    @Nullable PaymentStatus getPaymentStatus(UUID paymentId);

}
