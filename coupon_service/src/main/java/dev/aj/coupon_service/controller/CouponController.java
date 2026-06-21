package dev.aj.coupon_service.controller;

import dev.aj.coupon_service.service.CouponService;
import dev.aj.order_service.model.coupon.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(
        path = "${coupon_service.uri}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @GetMapping("/{couponCode}")
    public ResponseEntity<Coupon> getCoupon(@PathVariable UUID couponCode) {
        return ResponseEntity.ok(couponService.getCoupon(couponCode));
    }
}
