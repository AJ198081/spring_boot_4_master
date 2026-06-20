package dev.aj.customer_service.service.impl;

import dev.aj.customer_service.service.CustomerService;
import dev.aj.order_service.model.common.Address;
import dev.aj.order_service.model.common.Country;
import dev.aj.order_service.model.customer.Customer;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private static final ConcurrentHashMap<UUID, Customer> CUSTOMERS = new ConcurrentHashMap<>();

    private final Faker faker;

    @Override
    public Customer getCustomerByIdentifier(UUID customerId) {

        return CUSTOMERS.computeIfAbsent(customerId, newCustomerId -> new Customer.RetailCustomer(
                newCustomerId,
                "AJ",
                new Address.Residential(faker.address().streetAddress(), faker.address().city(), faker.address().state(), faker.address().postcode(), Country.INDIA)
        ));
    }

}
