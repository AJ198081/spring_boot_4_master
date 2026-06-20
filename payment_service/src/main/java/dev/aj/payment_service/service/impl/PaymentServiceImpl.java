package dev.aj.payment_service.service.impl;

import dev.aj.order_service.model.payment.PaymentRequest;
import dev.aj.order_service.model.payment.PaymentStatus;
import dev.aj.payment_service.service.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
@NullMarked
public class PaymentServiceImpl implements PaymentService {

    private final ConcurrentHashMap<PaymentRequest, PaymentStatus> processedPayments = new ConcurrentHashMap<>();

    @Override
    @Transactional
    public PaymentStatus process(PaymentRequest paymentRequest) {
        log.info("Processing payment request {}", paymentRequest);
        return processedPayments.computeIfAbsent(
                paymentRequest,
                newPaymentRequest -> new PaymentStatus.Completed(newPaymentRequest, UUID.randomUUID()));
    }


    @Override
    public @Nullable PaymentStatus getPaymentStatus(UUID paymentId) {
        return null;
    }
}
