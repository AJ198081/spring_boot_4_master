package dev.aj.order_service.service.impl;

import dev.aj.order_service.client.CustomerClient;
import dev.aj.order_service.client.ProductClient;
import dev.aj.order_service.model.customer.Customer;
import dev.aj.order_service.model.exception.ApplicationException;
import dev.aj.order_service.model.exception.DomainError;
import dev.aj.order_service.model.order.Order;
import dev.aj.order_service.model.order.OrderItem;
import dev.aj.order_service.model.order.OrderRequest;
import dev.aj.order_service.model.product.Product;
import dev.aj.order_service.model.product.ProductStatus;
import dev.aj.order_service.service.OrderValidatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@NullMarked
public class OrderValidatorServiceImpl implements OrderValidatorService {

    private final ProductClient productClient;
    private final CustomerClient customerClient;

    @Override
    public Order validate(OrderRequest orderRequest) {

        log.info("Validating order request {}", orderRequest);

        Product product = this.getProduct(orderRequest.productId());
        Customer customer = customerClient.getCustomer(orderRequest.customerId());

        return new Order(
                UUID.randomUUID(),
                customer,
                List.of(new OrderItem(product, orderRequest.quantity())),
                LocalDate.now());
    }

    private Product getProduct(String productId) {
        return switch (productClient.getProduct(productId)) {
            case ProductStatus.Active activeProduct -> activeProduct.product();
            case ProductStatus.Discontinued discontinuedProduct ->
                    throw new ApplicationException(DomainError.ProductDiscontinued.of(discontinuedProduct.productId()));
        };
    }
}
