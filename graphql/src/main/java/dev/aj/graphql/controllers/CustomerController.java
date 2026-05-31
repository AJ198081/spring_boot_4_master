package dev.aj.graphql.controllers;

import dev.aj.graphql.model.entities.Customer;
import dev.aj.graphql.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Controller
public class CustomerController {

    private final CustomerService customerService;

    @QueryMapping(value = "all_customers")
    public List<Customer> getCustomers() {
        return customerService.getAllCustomers();
    }

    @QueryMapping(value = "customer_by_id")
    public Customer getCustomerById(@Argument(value = "id") Long customerId) {
        return customerService.getCustomerById(customerId);
    }

}
