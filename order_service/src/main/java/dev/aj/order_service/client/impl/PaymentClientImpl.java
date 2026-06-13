package dev.aj.order_service.client.impl;

import dev.aj.order_service.client.AbstractServiceClient;
import dev.aj.order_service.client.PaymentClient;
import dev.aj.order_service.model.payment.PaymentRequest;
import dev.aj.order_service.model.payment.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@NullMarked
public class PaymentClientImpl extends AbstractServiceClient implements PaymentClient {

    private final RestClient paymentClient;

    @Override
    public PaymentStatus processs(PaymentRequest paymentRequest) {
        return executeRequest(() -> Objects.requireNonNull(paymentClient.post()
                .uri("/payments")
                .body(paymentRequest)
                .retrieve()
                .body(PaymentStatus.class))
        );
    }

    @Override
    public @Nullable PaymentStatus getPaymentStatus(UUID orderId) {

        return executeRequest(() -> paymentClient.get()
                .uri("/payments/{paymentId}", orderId))
                .retrieve()
                .body(PaymentStatus.class);
    }

    @Override
    protected String getServiceName() {
        return "payment-service";
    }
}
