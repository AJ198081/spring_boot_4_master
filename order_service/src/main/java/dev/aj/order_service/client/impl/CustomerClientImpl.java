package dev.aj.order_service.client.impl;

import dev.aj.order_service.client.AbstractServiceClient;
import dev.aj.order_service.client.CustomerClient;
import dev.aj.order_service.model.customer.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Service
@Slf4j
public class CustomerClientImpl extends AbstractServiceClient implements CustomerClient {

    private final RestClient customerClient;

    public CustomerClientImpl(RestClient customerClient, Environment environment) {
        super(environment);
        this.customerClient = customerClient;
    }

    @Override
    public Customer getCustomer(UUID customerId) {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer Id cannot be null");
        }

        return executeRequest(() -> customerClient.get()
                .uri("/{customerId}", customerId)
                .retrieve()
                .body(Customer.class)
        );
    }
}
