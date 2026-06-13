package dev.aj.order_service.model.product;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Product.Single.class, name = "Single"),
        @JsonSubTypes.Type(value = Product.Bundle.class, name = "Bundle")
})
public sealed interface Product {

    record Single(String productId, String name, double price) implements Product {
    }

    record Bundle(String productId, String name, double originalPrice, double discountedPrice,
                  List<Single> items) implements Product {
    }

}
