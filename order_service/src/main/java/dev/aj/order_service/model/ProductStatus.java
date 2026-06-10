package dev.aj.order_service.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.LocalDate;

@JsonTypeInfo(use = JsonTypeInfo.Id.SIMPLE_NAME,
        property = "status",
        include = JsonTypeInfo.As.EXTERNAL_PROPERTY
)
public sealed interface ProductStatus {

    record Active(Product product) implements ProductStatus {
    }

    record Discontinued(String productId, String reason, LocalDate discontinuedOnDate) implements ProductStatus {
    }

}
