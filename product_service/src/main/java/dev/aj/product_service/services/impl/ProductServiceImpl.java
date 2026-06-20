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

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final Faker faker;

    @Override
    public ProductStatus getProduct(String productId) {

        log.info("Fetching product with id {}", productId);

        return new ProductStatus.Active(
                new Product.Single(
                        productId,
                        faker.commerce().productName(),
                        new NonNegativeAmount(BigDecimal.valueOf(faker.random().nextDouble(15, 300)))
                )
        );
    }
}
