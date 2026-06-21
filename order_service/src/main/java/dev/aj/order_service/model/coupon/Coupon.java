package dev.aj.order_service.model.coupon;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.aj.order_service.model.common.NonNegativeAmount;
import dev.aj.order_service.model.common.Rate;

import java.util.UUID;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.SIMPLE_NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
public sealed interface Coupon {
    record NoDiscount() implements Coupon {
    }

    record FlatDiscount(UUID couponId, NonNegativeAmount amount) implements Coupon {
    }

    record PercentageDiscount(UUID couponId, Rate percentage) implements Coupon {
    }

    record MaxPercentageDiscount(UUID couponId,
                                 Rate percentage,
                                 NonNegativeAmount maxDiscountAmount) implements Coupon {
    }

    record FreeShipping(UUID couponId, NonNegativeAmount discountAmount) implements Coupon {
    }
}
