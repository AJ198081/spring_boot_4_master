package dev.aj.coupon_service.service.impl;

import dev.aj.coupon_service.DiscountTypeCodes;
import dev.aj.coupon_service.service.CouponService;
import dev.aj.order_service.model.common.NonNegativeAmount;
import dev.aj.order_service.model.common.Rate;
import dev.aj.order_service.model.coupon.Coupon;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static java.util.UUID.fromString;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableConfigurationProperties(DiscountTypeCodes.class)
public class CouponServiceImpl implements CouponService {

    private final DiscountTypeCodes discountTypeCodes;

    @Override
    public Coupon getCoupon(UUID couponCode) {

        if (Objects.equals(fromString(discountTypeCodes.flat().getFirst()), couponCode)) {
            return new Coupon.FlatDiscount(couponCode,
                    getDiscountAmount(discountTypeCodes.flat().get(1))
            );
        }

        if (fromString(String.valueOf(discountTypeCodes.percentage().getFirst())).equals(couponCode)) {
            return new Coupon.PercentageDiscount(couponCode,
                    getDiscountPercentage(discountTypeCodes.percentage()));
        }

        if (fromString(discountTypeCodes.maxPercentage().getFirst()).equals(couponCode)) {
            return new Coupon.MaxPercentageDiscount(couponCode,
                    getDiscountPercentage(discountTypeCodes.maxPercentage()),
                    getDiscountAmount(discountTypeCodes.maxPercentage().get(2)));
        }

        if (fromString(discountTypeCodes.freeShipping().getFirst()).equals(couponCode)) {
            return new Coupon.FlatDiscount(couponCode,
                    getDiscountAmount(discountTypeCodes.freeShipping().get(1)));
        }

        return new Coupon.NoDiscount();
    }

    private @NonNull Rate getDiscountPercentage(List<String> discountTypeCodes) {
        return new Rate(Double.parseDouble(discountTypeCodes.get(1))
        );
    }

    private @NonNull NonNegativeAmount getDiscountAmount(String amount) {
        return new NonNegativeAmount(BigDecimal.valueOf(Double.parseDouble(amount)));
    }

}
