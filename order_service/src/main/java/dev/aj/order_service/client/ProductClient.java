package dev.aj.order_service.client;

import dev.aj.order_service.model.product.ProductStatus;

public interface ProductClient {

    ProductStatus getProduct(String productId);

}
