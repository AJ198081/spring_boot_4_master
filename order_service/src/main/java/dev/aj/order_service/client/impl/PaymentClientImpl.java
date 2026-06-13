package dev.aj.order_service.client.impl;

import dev.aj.order_service.client.AbstractServiceClient;
import dev.aj.order_service.client.PaymentClient;
import dev.aj.order_service.model.payment.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentClientImpl extends AbstractServiceClient implements PaymentClient {

    private final RestClient paymentClient;

    @Override
    public PaymentStatus getPaymentStatus(UUID orderId) {

        if (orderId == null) {
            throw new IllegalArgumentException("Payment Id cannot be null");
        }

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
