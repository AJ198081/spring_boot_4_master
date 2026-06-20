package dev.aj.customer_service.service.impl;

import dev.aj.customer_service.service.CustomerService;
import dev.aj.order_service.model.common.Address;
import dev.aj.order_service.model.common.Country;
import dev.aj.order_service.model.customer.Customer;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final Faker faker;

    @Override
    public Customer getCustomerByIdentifier(UUID customerId) {
        return new Customer.RetailCustomer(customerId,
                "AJ",
                new Address.Residential(faker.address().streetAddress(), faker.address().city(), faker.address().state(), faker.address().postcode(), Country.INDIA)
        );
    }

}
