package dev.aj.accounts.customer.controllers;

import dev.aj.accounts.common.domain.dtos.AddressRequest;
import dev.aj.accounts.common.domain.dtos.CustomerRequest;
import dev.aj.accounts.common.domain.dtos.CustomerResponse;
import dev.aj.accounts.setup.TestConfig;
import dev.aj.accounts.setup.TestDataFactory;
import dev.aj.accounts.setup.helpers.HelperMethods;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(value = {TestConfig.class, TestDataFactory.class, HelperMethods.class})
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerControllerTest {

    @Autowired
    private TestConfig testConfig;

    @Autowired
    private TestDataFactory testDataFactory;

    @Autowired
    private Environment environment;

    @LocalServerPort
    private int port;

    private RestClient customerRestClient;

    @BeforeAll
    void beforeAll() {
        customerRestClient = testConfig.restClient("http://localhost:%d%s"
                .formatted(port, environment.getRequiredProperty("api.basePath.customers", String.class)));
    }

    @AfterAll
    void afterAll() {
        customerRestClient = null;
    }

    @Test
    void createCustomerSuccessfully() {

        CustomerRequest customerRequest = testDataFactory.generateCustomerRequest();

        ResponseEntity<Void> customerResponseEntity = registerNewCustomer(customerRequest);

        assertTrue(customerResponseEntity.getStatusCode().isSameCodeAs(HttpStatus.CREATED));

        UUID customerId = extractUuidFromResponseEntity(customerResponseEntity);

        assertThat(customerId.toString())
                .isNotNull()
                .matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
    }

    @Test
    void createCustomerWithDuplicateEmailReturnsConflict() {
        CustomerRequest customerRequest = testDataFactory.generateCustomerRequest();
        registerNewCustomer(customerRequest);

        try{
            assertThat(registerNewCustomer(customerRequest).getStatusCode())
                    .isEqualTo(HttpStatus.CONFLICT);
        } catch (HttpClientErrorException e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        }
    }

    @Test
    void createCustomerWithAddress() {

        CustomerRequest customerRequest = testDataFactory.generateCustomerRequest();
        AddressRequest addressRequest = testDataFactory.generateAnAddressRequest();
        customerRequest.setAddresses(Set.of(addressRequest));

        ResponseEntity<Void> registrationResponse = registerNewCustomer(customerRequest);
        UUID customerId = extractUuidFromResponseEntity(registrationResponse);
        CustomerResponse customerResponse = getCustomerByCustomerId(customerId).getBody();

        assert customerResponse != null;
        assertThat(customerResponse.addresses().stream().findFirst().orElse(null))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(addressRequest);
    }

    @Test
    void getAnExistingCustomerSuccessfully() {
        CustomerRequest newCustomerRequest = testDataFactory.generateCustomerRequest();

        ResponseEntity<Void> registeredCustomerResponse = registerNewCustomer(newCustomerRequest);

        UUID registeredCustomerId = extractUuidFromResponseEntity(registeredCustomerResponse);
        ResponseEntity<CustomerResponse> fetchedCustomerResponse = getCustomerByCustomerId(registeredCustomerId);

        assertTrue(fetchedCustomerResponse.getStatusCode().is2xxSuccessful());

        assertThat(fetchedCustomerResponse.getBody())
                .isNotNull()
                .satisfies(customerResponse -> {
                    assert customerResponse != null;
                    assertThat(customerResponse.customerId()).isNotNull();
                    assertThat(customerResponse.customerId()).isEqualTo(registeredCustomerId);
                });

        assertThat(fetchedCustomerResponse.getBody())
                .usingRecursiveComparison().ignoringFields("customerId", "addresses")
                .isEqualTo(newCustomerRequest);

    }

    @Test
    void getNonExistingCustomerReturnsNotFound() {

        try {
            getCustomerByCustomerId(UUID.randomUUID());
        } catch (HttpClientErrorException e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

    }

    private ResponseEntity<Void> registerNewCustomer(CustomerRequest customerRequest) {
        return customerRestClient.post()
                .uri("/")
                .body(customerRequest)
                .retrieve()
                .toBodilessEntity();
    }

    private static @NonNull UUID extractUuidFromResponseEntity(ResponseEntity<Void> customerResponseEntity) {
        String locationPath = Objects.requireNonNull(customerResponseEntity.getHeaders().getLocation()).getPath();
        return UUID.fromString(locationPath.substring(locationPath.lastIndexOf('/') + 1));
    }

    private ResponseEntity<CustomerResponse> getCustomerByCustomerId(UUID registeredCustomerId) {
        return customerRestClient.get()
                .uri("/{customerId}", registeredCustomerId)
                .retrieve()
                .toEntity(CustomerResponse.class);
    }
}