package dev.aj.order_service.client;

import dev.aj.order_service.model.coupon.Coupon;

import java.util.UUID;

public interface CouponClient {
    Coupon getCoupon(UUID couponCode);
}
