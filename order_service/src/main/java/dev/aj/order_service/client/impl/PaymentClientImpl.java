package dev.aj.order_service.client.impl;

import dev.aj.order_service.client.AbstractServiceClient;
import dev.aj.order_service.client.PaymentClient;
import dev.aj.order_service.model.invoice.Invoice;
import dev.aj.order_service.model.payment.PaymentRequest;
import dev.aj.order_service.model.payment.PaymentStatus;
import dev.aj.order_service.model.payment.RefundRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@NullMarked
@RequiredArgsConstructor
public class PaymentClientImpl extends AbstractServiceClient implements PaymentClient {

    private final RestClient paymentClient;

    @Override
    public PaymentStatus process(PaymentRequest paymentRequest) {
        return executeRequest(() -> Objects.requireNonNull(paymentClient.post()
                .uri("/")
                .body(paymentRequest)
                .retrieve()
                .body(PaymentStatus.class))
        );
    }

    @Override
    public @Nullable PaymentStatus getPaymentStatus(UUID orderId) {

        return executeRequest(() -> paymentClient.get()
                .uri("/{paymentId}", orderId))
                .retrieve()
                .body(PaymentStatus.class);
    }

    @Override
    public void refund(RefundRequest refundRequest) {
        this.executeRequest(() -> paymentClient.post()
                .uri("/refund")
                .body(refundRequest)
                .retrieve()
                .toBodilessEntity()
        );
    }

    @Override
    public void refund(Invoice invoice) {

        log.info("Refunding invoice {}", invoice);

        switch (invoice) {

            case Invoice.Paid paid -> this.executeRequest(() -> paymentClient.put()
                    .uri("/refund/{invoiceId}", paid.invoiceId())
                    .retrieve().toBodilessEntity());

            case Invoice.Unpaid _ -> log.info("Invoice is not paid, no refund needed");
        }
    }

    @Override
    public String getServiceName() {
        return "payment-service";
    }
}
