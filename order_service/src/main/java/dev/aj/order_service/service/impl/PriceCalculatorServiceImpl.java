package dev.aj.order_service.service.impl;

import dev.aj.order_service.model.common.Country;
import dev.aj.order_service.model.common.NonNegativeAmount;
import dev.aj.order_service.model.common.PriceSummary;
import dev.aj.order_service.model.common.Rate;
import dev.aj.order_service.model.coupon.Coupon;
import dev.aj.order_service.model.order.Order;
import dev.aj.order_service.model.order.OrderItem;
import dev.aj.order_service.model.product.Product;
import dev.aj.order_service.service.PriceCalculatorService;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Service;

@NullMarked
@Service
public class PriceCalculatorServiceImpl implements PriceCalculatorService {

    @Override
    public PriceSummary calculate(Order order) {

        NonNegativeAmount totalPrice = order.items()
                .stream()
                .map(OrderItem::product)
                .map(Product::price)
                .reduce(NonNegativeAmount.ZERO(), NonNegativeAmount::add);

        NonNegativeAmount totalDiscountedPrice = switch (order.coupon()) {

            case Coupon.NoDiscount _ -> totalPrice;

            case Coupon.FlatDiscount flatDiscount -> totalPrice.greaterThan(flatDiscount.amount())
                    ? totalPrice.subtract(flatDiscount.amount())
                    : NonNegativeAmount.ZERO();

            case Coupon.FreeShipping freeShipping -> totalPrice.greaterThan(freeShipping.discountAmount())
                    ? totalPrice.subtract(freeShipping.discountAmount())
                    : NonNegativeAmount.ZERO();

            case Coupon.PercentageDiscount(_, Rate percentageDiscount) ->
                    totalPrice.greaterThan(NonNegativeAmount.ZERO())
                            ? totalPrice.percentage(percentageDiscount)
                            : NonNegativeAmount.ZERO();

            case Coupon.MaxPercentageDiscount(_, Rate rate, NonNegativeAmount maxDiscountAmount) -> {
                NonNegativeAmount percentageDiscountAmount = totalPrice.percentage(rate);

                NonNegativeAmount effectiveDiscountAmount = percentageDiscountAmount.greaterThan(maxDiscountAmount)
                        ? maxDiscountAmount
                        : percentageDiscountAmount;

                yield totalPrice.subtract(effectiveDiscountAmount);
            }
        };

        NonNegativeAmount totalTaxAmount = NonNegativeAmount.of(this.getTaxRate(order.customer().address().country()))
                .multiply(totalDiscountedPrice);

        return new PriceSummary(
                totalPrice,
                totalDiscountedPrice,
                totalTaxAmount,
                totalTaxAmount.add(totalDiscountedPrice)
        );
    }

    private NonNegativeAmount priceToBeChargedForProduct(Product product) {
        return switch (product) {
            case Product.Single singleProduct -> singleProduct.price();
            case Product.Bundle bundleProduct -> bundleProduct.discountedPrice();
        };
    }


    private Rate getTaxRate(Country country) {

        return switch (country) {
            case AUSTRALIA -> new Rate(10);
            case CANADA -> new Rate(5);
            case US -> new Rate(7);
            case INDIA -> new Rate(18);
            case NZ -> new Rate(15);
        };
    }

}
