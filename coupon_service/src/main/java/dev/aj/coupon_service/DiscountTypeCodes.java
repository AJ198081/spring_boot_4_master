package dev.aj.coupon_service;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "coupon.type")
public record DiscountTypeCodes(
        List<String> flat,
        List<String> percentage,
        List<String> maxPercentage,
        List<String> freeShipping
) {
}
