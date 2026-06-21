package dev.aj.order_service.client.impl;

import dev.aj.order_service.client.AbstractServiceClient;
import dev.aj.order_service.client.CouponClient;
import dev.aj.order_service.model.coupon.Coupon;
import dev.aj.order_service.model.exception.RetryableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CouponClientImpl extends AbstractServiceClient implements CouponClient {

    private final RestClient couponClient;

    @Retryable(
            maxDelay = 30000,
            includes = {RetryableException.class},
            delay = 1500L,
            multiplier = 2,
            jitter = 50,
            maxRetries = 4
    )
    @Override
    public Coupon getCoupon(UUID couponCode) {
        return executeRequest(() -> couponClient.get()
                .uri("/{couponCode}", couponCode)
                .retrieve()
                .body(Coupon.class));
    }

    @Override
    public String getServiceName() {
        return "coupon-service";
    }
}
