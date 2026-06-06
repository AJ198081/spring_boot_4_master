package dev.aj.graphql.controllers;

import dev.aj.graphql.model.entities.Customer;
import dev.aj.graphql.model.entities.Order;
import dev.aj.graphql.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @QueryMapping(value = "customer_name_contains")
    public List<Customer> getCustomerWithMatchingName(@Argument(value = "name_pattern") String namePattern) {
        return customerService.getCustomersWithMatchingNamePatterns(namePattern);
    }

    @QueryMapping(value = "customer_of_age_range")
    public List<Customer> getCustomersOfAgeRange(@Argument(value = "age_range") AgeRange ageRange) {
        return customerService.getCustomersByAgeRange(ageRange);
    }

    @SchemaMapping(typeName = "Customer", field = "orders")
    public List<Order> orders(@NonNull Customer customer, @Argument @NonNull Integer limit) {
        return customer.getOrders()
                .stream()
                .limit(limit)
                .toList();
    }

    @BatchMapping(typeName = "Customer", field = "orders")
    public Map<Customer, List<Order>> allOrders(@NonNull List<Customer> customers) {
        return customers.stream()
                .map(customer -> {
                    Customer newCustomer = customer.toNewCustomer();
                    newCustomer.setOrders(customer.getOrders());
                    return newCustomer;
                })
                .collect(Collectors.toMap(
                        Function.identity(),
                        Customer::getOrders
                ));
    }

    public record AgeRange(int minAge, int maxAge) { }

}
