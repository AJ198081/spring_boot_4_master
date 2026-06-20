package dev.aj.product_service.services.impl;

import dev.aj.order_service.model.common.NonNegativeAmount;
import dev.aj.order_service.model.product.Product;
import dev.aj.order_service.model.product.ProductStatus;
import dev.aj.product_service.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private static final ConcurrentHashMap<String, ProductStatus> PRODUCTS = new ConcurrentHashMap<>();

    private final Faker faker;

    @Override
    public ProductStatus getProduct(String productId) {

        log.info("Fetching a product with id {}", productId);

        return PRODUCTS.computeIfAbsent(productId, newProductId -> new ProductStatus.Active(
                new Product.Single(
                        newProductId,
                        faker.commerce().productName(),
                        new NonNegativeAmount(BigDecimal.valueOf(faker.random().nextDouble(15, 300)))
                )
        ));
    }
}
