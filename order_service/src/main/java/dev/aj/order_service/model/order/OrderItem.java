package dev.aj.order_service.model.order;

import dev.aj.order_service.model.product.Product;

public record OrderItem(Product product, int quantity) {
}
