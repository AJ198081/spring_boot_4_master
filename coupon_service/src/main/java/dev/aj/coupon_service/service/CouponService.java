package dev.aj.coupon_service.service;

import dev.aj.order_service.model.coupon.Coupon;

import java.util.UUID;

public interface CouponService {
    Coupon getCoupon(UUID couponCode);
}
