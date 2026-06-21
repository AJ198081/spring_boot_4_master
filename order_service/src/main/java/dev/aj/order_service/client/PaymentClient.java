package dev.aj.order_service.client;

import dev.aj.order_service.model.invoice.Invoice;
import dev.aj.order_service.model.payment.PaymentRequest;
import dev.aj.order_service.model.payment.PaymentStatus;
import dev.aj.order_service.model.payment.RefundRequest;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

@NullMarked
public interface PaymentClient {

    PaymentStatus process(PaymentRequest paymentRequest);
    @Nullable PaymentStatus getPaymentStatus(UUID paymentId);
    void refund(RefundRequest refundRequest);

    void refund(Invoice invoice);

}
