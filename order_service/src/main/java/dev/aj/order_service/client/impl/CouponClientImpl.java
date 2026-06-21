package dev.aj.order_service.client.impl;

import dev.aj.order_service.client.AbstractServiceClient;
import dev.aj.order_service.client.CouponClient;
import dev.aj.order_service.model.coupon.Coupon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Service
@Slf4j
public class CouponClientImpl extends AbstractServiceClient implements CouponClient {

    private final RestClient couponClient;

    public CouponClientImpl(RestClient couponClient, Environment environment) {
        super(environment);
        this.couponClient = couponClient;
    }

    @Override
    public Coupon getCoupon(UUID couponCode) {
        return executeRequest(() -> couponClient.get()
                .uri("/{couponCode}", couponCode)
                .retrieve()
                .body(Coupon.class));
    }
}
