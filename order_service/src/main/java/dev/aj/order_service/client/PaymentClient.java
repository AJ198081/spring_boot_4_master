package dev.aj.order_service.client;

import dev.aj.order_service.model.payment.PaymentRequest;
import dev.aj.order_service.model.payment.PaymentStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

@NullMarked
public interface PaymentClient {

    PaymentStatus processs(PaymentRequest paymentRequest);
    @Nullable PaymentStatus getPaymentStatus(UUID paymentId);

}
