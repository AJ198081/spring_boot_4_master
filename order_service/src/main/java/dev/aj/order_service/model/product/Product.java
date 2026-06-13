package dev.aj.order_service.model.product;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.aj.order_service.model.common.NonNegativeAmount;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Product.Single.class, name = "Single"),
        @JsonSubTypes.Type(value = Product.Bundle.class, name = "Bundle")
})
public sealed interface Product {

    String productId();
    String name();
    NonNegativeAmount price();

    record Single(String productId, String name, NonNegativeAmount price) implements Product {
    }

    record Bundle(String productId, String name, NonNegativeAmount originalPrice, NonNegativeAmount discountedPrice,
                  List<Single> items) implements Product {

        @Override
        public NonNegativeAmount price() {
            return this.originalPrice;
        }
    }

}
