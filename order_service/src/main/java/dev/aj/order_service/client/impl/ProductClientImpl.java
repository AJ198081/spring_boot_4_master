package dev.aj.order_service.client.impl;

import dev.aj.order_service.client.AbstractServiceClient;
import dev.aj.order_service.client.ProductClient;
import dev.aj.order_service.model.exception.ApplicationException;
import dev.aj.order_service.model.exception.DomainError;
import dev.aj.order_service.model.product.ProductStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class ProductClientImpl extends AbstractServiceClient implements ProductClient {

    private final RestClient productClient;

    public ProductClientImpl(RestClient productClient, Environment environment) {
        super(environment);
        this.productClient = productClient;
    }

    @Override
    public ProductStatus getProduct(String productId) {

        if (productId != null) {

          return executeRequest(() ->
                    this.productClient.get()
                            .uri("/{productId}", productId)
                            .retrieve()
                            .body(ProductStatus.class));
        }

        throw new ApplicationException(new DomainError.MissingArgument("Product Id cannot be null or empty"));
    }

    @Override
    protected String getServiceName() {
        return "product-service";
    }
}
