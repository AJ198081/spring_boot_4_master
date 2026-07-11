package dev.aj.bank_customer.controllers;

import dev.aj.bank_commons.types.Email;
import dev.aj.bank_customer.bootstrap.Config;
import dev.aj.bank_customer.model.dtos.AddressRequest;
import dev.aj.bank_customer.model.dtos.CustomerCreatedResponse;
import dev.aj.bank_customer.model.dtos.CustomerRequest;
import net.datafaker.Faker;
import net.datafaker.providers.base.Address;
import net.datafaker.providers.base.Superhero;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringJUnitConfig(classes = Config.class)
@TestPropertySource(value = {
        "classpath:application.properties",
        "classpath:junit-platform.properties"
})
@Execution(ExecutionMode.CONCURRENT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerControllerTest {

    @Autowired
    private Environment environment;

    @Autowired
    private Faker faker;

    private RestTestClient restTestClient;


    @BeforeAll
    void setUp() {
        restTestClient = RestTestClient.bindToServer()
                .baseUrl("http://localhost:%s%s"
                        .formatted(
                                environment.getRequiredProperty("server.port"),
                                environment.getRequiredProperty("bank_customer_uri"))
                )
                .build();

    }

    @Test
    void testCustomerGetsCreatedSuccessfully_WhenValidRequestIsSent() {

        restTestClient.post()
                .uri("/")
                .body(getStreamOfCustomerRequests().limit(1).findAny().orElseThrow())
                .exchange()
                .expectAll(responseSpec -> responseSpec.expectStatus().isCreated(),
                        responseSpec -> responseSpec.expectBody(new ParameterizedTypeReference<CustomerCreatedResponse>() {
                        }).consumeWith(createdCustomerResponse -> {
                            createdCustomerResponse.getResponseHeaders().containsHeader(HttpHeaders.LOCATION);
                            URI location = createdCustomerResponse.getResponseHeaders().getLocation();
                            assert createdCustomerResponse.getResponseBody() != null;
                            assert location != null;
                            UUID uuid = createdCustomerResponse.getResponseBody().externalId();
                            String createdCustomerId = location.getPath().substring(location.getPath().lastIndexOf('/') + 1);
                            assert uuid.equals(UUID.fromString(createdCustomerId));
                        }));


    }

    @Test
    void testAsync_CustomerGetsCreatedSuccessfully_WhenValidRequestIsSent() {

        restTestClient.post()
                .uri("/")
                .headers(headers -> {
                            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            headers.add(environment.getRequiredProperty("async.request.processing.header"), "true");
                        }
                )
                .body(getStreamOfCustomerRequests().limit(1).findAny().orElseThrow())
                .exchange()
                .expectAll(responseSpec -> responseSpec.expectStatus().isCreated(),
                        responseSpec -> responseSpec.expectBody(new ParameterizedTypeReference<CustomerCreatedResponse>() {
                        }).consumeWith(createdCustomerResponse -> {
                            createdCustomerResponse.getResponseHeaders().containsHeader(HttpHeaders.LOCATION);
                            URI location = createdCustomerResponse.getResponseHeaders().getLocation();
                            assert location != null;
                        }));


    }


    @RepeatedTest(value = 1000, name = "{currentRepetition}/{totalRepetitions}")
    void testConcurrentAsync_CustomerGetsCreatedSuccessfully_WhenValidRequestIsSent() {

        restTestClient.post()
                .uri("/")
                .headers(headers -> {
                            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            headers.add(environment.getRequiredProperty("async.request.processing.header"), "true");
                        }
                )
                .body(getStreamOfCustomerRequests().limit(1).findAny().orElseThrow())
                .exchange()
                .expectAll(responseSpec -> responseSpec.expectStatus().isCreated(),
                        responseSpec -> responseSpec.expectBody(new ParameterizedTypeReference<CustomerCreatedResponse>() {
                        }).consumeWith(createdCustomerResponse -> {
                            createdCustomerResponse.getResponseHeaders().containsHeader(HttpHeaders.LOCATION);
                            URI location = createdCustomerResponse.getResponseHeaders().getLocation();
                            assert location != null;
                        }));


    }

    @RepeatedTest(value = 1000, name = "{currentRepetition}/{totalRepetitions}")
    void testConcurrent_CustomersCreatedSuccessfully_WhenValidRequestIsSent() {

        restTestClient.post()
                .uri("/")
                .body(getStreamOfCustomerRequests().limit(1).findAny().orElseThrow())
                .exchange()
                .expectAll(responseSpec -> responseSpec.expectStatus().isCreated(),
                        responseSpec -> responseSpec.expectBody(new ParameterizedTypeReference<CustomerCreatedResponse>() {
                        }).consumeWith(createdCustomerResponse -> {
                            createdCustomerResponse.getResponseHeaders().containsHeader(HttpHeaders.LOCATION);
                            URI location = createdCustomerResponse.getResponseHeaders().getLocation();
                            assert createdCustomerResponse.getResponseBody() != null;
                            assert location != null;
                            UUID uuid = createdCustomerResponse.getResponseBody().externalId();
                            String createdCustomerId = location.getPath().substring(location.getPath().lastIndexOf('/') + 1);
                            assert uuid.equals(UUID.fromString(createdCustomerId));
                        }));


    }

    private Stream<CustomerRequest> getStreamOfCustomerRequests() {
        return Stream.generate(() -> {
            Superhero superhero = faker.superhero();

            Address address = faker.address();
            return new CustomerRequest(
                    superhero.name(),
                    faker.name().lastName(),
                    new Email(faker.internet().emailAddress()),
                    new AddressRequest(
                            AddressRequest.AddressType.HOME,
                            address.streetAddressNumber(),
                            address.streetAddress(),
                            address.city(),
                            address.state(),
                            address.postcode(),
                            address.country()
                    ),
                    faker.phoneNumber().phoneNumber()
            );
        });
    }

}