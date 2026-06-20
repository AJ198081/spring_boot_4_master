package dev.aj.product_service.services;

import dev.aj.order_service.model.product.ProductStatus;

public interface ProductService {
    ProductStatus getProduct(String productId);
}
