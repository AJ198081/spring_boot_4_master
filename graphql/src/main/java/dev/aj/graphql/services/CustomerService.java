package dev.aj.graphql.services;

import dev.aj.graphql.controllers.CustomerController;
import dev.aj.graphql.model.entities.Customer;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final Faker faker;
    private final OrderService orderService;
    @Getter
    private final List<Customer> customers = new ArrayList<>();
    private final JsonMapper jsonMapper;

    @PostConstruct
    public void init() {

        customers.clear();

        customers.addAll(getCustomerStream()
                .limit(50)
                .toList());

      log.info("Customers initialized {}", jsonMapper.writeValueAsString(customers));
    }

    public List<Customer> getAllCustomers() {
        return this.customers;
    }

    public @Nullable Customer getCustomerById(Long customerId) {
        return this.customers.stream()
                .filter(customer -> customer.getId().equals(customerId))
                .findFirst()
                .orElse(null);
    }

    private Stream<Customer> getCustomerStream() {
        return Stream.generate(() -> Customer.create(
                faker.random().nextLong(),
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().emailAddress(),
                faker.random().nextInt(18, 99),
                IntStream.rangeClosed(0, faker.random().nextInt(0, 5))
                        .mapToObj(_ -> orderService.generateARandomOrder())
                        .toList()
        ));
    }

    public List<Customer> getCustomersWithMatchingNamePatterns(String namePattern) {
        return this.customers.stream().filter(customer -> customer.getFirstName().contains(namePattern) || customer.getLastName().contains(namePattern)).toList();
    }

    public List<Customer> getCustomersByAgeRange(CustomerController.AgeRange ageRange) {

        Predicate<Customer> customerInAgeRange = customer -> customer.getAge() > ageRange.minAge() && customer.getAge() < ageRange.maxAge();

        return this.customers.stream()
                .filter(customerInAgeRange)
                .toList();
    }

}


