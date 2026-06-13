package dev.aj.order_service.client.impl;

import dev.aj.order_service.client.AbstractServiceClient;
import dev.aj.order_service.client.CustomerClient;
import dev.aj.order_service.model.customer.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerClientImpl extends AbstractServiceClient implements CustomerClient {

    private final RestClient customerClient;

    @Override
    public Customer getCustomer(UUID customerId) {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer Id cannot be null");
        }

        return executeRequest(() -> customerClient.get()
                .uri("/customers/{customerId}", customerId)
                .retrieve()
                .body(Customer.class)
        );
    }

    @Override
    protected String getServiceName() {
        return "customer-service";
    }
}
