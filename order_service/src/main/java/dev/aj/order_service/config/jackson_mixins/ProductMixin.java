package dev.aj.order_service.config.jackson_mixins;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.boot.jackson.JacksonMixin;

import static dev.aj.order_service.model.product.Product.Bundle;
import static dev.aj.order_service.model.product.Product.Single;

//@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION, property = "type", include = JsonTypeInfo.As.PROPERTY)
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Single.class),
        @JsonSubTypes.Type(value = Bundle.class)
})
@JacksonMixin(dev.aj.order_service.model.product.Product.class)
public class ProductMixin {
}
