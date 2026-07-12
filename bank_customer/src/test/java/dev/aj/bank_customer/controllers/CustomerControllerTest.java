package dev.aj.bank_customer.controllers;

import dev.aj.bank_commons.types.Email;
import dev.aj.bank_customer.bootstrap.Config;
import dev.aj.bank_customer.model.dtos.AddressDto;
import dev.aj.bank_customer.model.dtos.CustomerCreatedResponse;
import dev.aj.bank_customer.model.dtos.CustomerRequest;
import dev.aj.bank_customer.model.dtos.CustomerResponse;
import dev.aj.bank_customer.model.dtos.KycStatus;
import net.datafaker.Faker;
import net.datafaker.providers.base.Address;
import net.datafaker.providers.base.Superhero;
import org.jspecify.annotations.NonNull;
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
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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

        postCustomerRequest()
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

    private RestTestClient.@NonNull ResponseSpec postCustomerRequest() {
        return restTestClient.post()
                .uri("/")
                .headers(getRequestHeaders(false))
                .body(getStreamOfCustomerRequests().limit(1).findAny().orElseThrow())
                .exchange();
    }

    @Test
    void testAsync_CustomerGetsCreatedSuccessfully_WhenValidRequestIsSent() {

        postAsyncCustomerRequest()
                .expectAll(responseSpec -> responseSpec.expectStatus().isCreated(),
                        responseSpec -> responseSpec.expectBody(new ParameterizedTypeReference<CustomerCreatedResponse>() {
                        }).consumeWith(createdCustomerResponse -> {
                            createdCustomerResponse.getResponseHeaders().containsHeader(HttpHeaders.LOCATION);
                            URI location = createdCustomerResponse.getResponseHeaders().getLocation();
                            assert location != null;
                        }));
    }


    @RepeatedTest(value = 1, name = "{currentRepetition}/{totalRepetitions}")
    void testConcurrentAsync_CustomerGetsCreatedSuccessfully_WhenValidRequestIsSent() {

        postAsyncCustomerRequest()
                .expectAll(responseSpec -> responseSpec.expectStatus().isCreated(),
                        responseSpec -> responseSpec.expectBody(new ParameterizedTypeReference<CustomerCreatedResponse>() {
                        }).consumeWith(createdCustomerResponse -> {
                            createdCustomerResponse.getResponseHeaders().containsHeader(HttpHeaders.LOCATION);
                            URI location = createdCustomerResponse.getResponseHeaders().getLocation();
                            assert location != null;
                        }));


    }

    private RestTestClient.@NonNull ResponseSpec postAsyncCustomerRequest() {
        return restTestClient.post()
                .uri("/")
                .headers(getRequestHeaders(true))
                .body(getStreamOfCustomerRequests().limit(1).findAny().orElseThrow())
                .exchange();
    }

    @RepeatedTest(value = 1, name = "{currentRepetition}/{totalRepetitions}")
    void testConcurrent_CustomersCreatedSuccessfully_WhenValidRequestIsSent() {

        postCustomerRequest()
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
    void testUpdateKycStatusSuccessfully_WhenValidRequestIsSent() {

        postCustomerRequest()
                .expectBody(new ParameterizedTypeReference<CustomerCreatedResponse>() {
                })
                .consumeWith(createdCustomerResponse -> {
                    assert createdCustomerResponse.getResponseBody() != null;
                    UUID customerId = createdCustomerResponse.getResponseBody().externalId();
                    restTestClient.patch()
                            .uri(uriBuilder -> uriBuilder.path("/{externalId}/kyc-status")
                                    .queryParam("kycStatus", KycStatus.APPROVED).build(customerId))
                            .headers(getRequestHeaders(false))
                            .exchange()
                            .expectAll(consumer -> {
                                consumer.expectStatus().isNoContent();
                                consumer.expectBody().isEmpty();
                                consumer.expectHeader().value(HttpHeaders.ETAG,
                                        version -> assertThat(Short.parseShort(version.replace("\"", "")))
                                                .isGreaterThanOrEqualTo((short) 1));
                            });
                });
    }

    @Test
    void testAsyncUpdateKycStatusSuccessfully_WhenValidRequestIsSent() {

        postCustomerRequest()
                .expectBody(new ParameterizedTypeReference<CustomerCreatedResponse>() {
                })
                .consumeWith(createdCustomerResponse -> {
                    assert createdCustomerResponse.getResponseBody() != null;
                    UUID customerId = createdCustomerResponse.getResponseBody().externalId();
                    restTestClient.patch()
                            .uri(uriBuilder -> uriBuilder.path("/{externalId}/kyc-status")
                                    .queryParam("kycStatus", KycStatus.APPROVED).build(customerId))
                            .headers(getRequestHeaders(true))
                            .exchange()
                            .expectAll(consumer -> consumer.expectStatus().isAccepted());
                });
    }

    @Test
    void testCustomerGetSuccessful_WhenValidExternalIdSent() {
        postCustomerRequest()
                .expectBody(new ParameterizedTypeReference<CustomerCreatedResponse>() {
                })
                .consumeWith(customerCreatedResponse -> {
                    CustomerCreatedResponse createdCustomerResponse = customerCreatedResponse.getResponseBody();

                    assert createdCustomerResponse != null;
                    UUID customerId = createdCustomerResponse.externalId();

                    restTestClient.get()
                            .uri("/{externalId}", customerId)
                            .headers(getRequestHeaders(false))
                            .exchange()
                            .expectStatus().isOk()
                            .expectAll(consumer -> consumer.expectBody(new ParameterizedTypeReference<CustomerResponse>() {})
                                    .consumeWith(customerResponse -> {
                                        assert customerResponse.getResponseBody() != null;
                                        assert customerResponse.getResponseBody().externalId().equals(customerId);
                                        assertThat(customerResponse.getResponseBody().createdAt().truncatedTo(ChronoUnit.MILLIS))
                                                .isEqualTo(createdCustomerResponse.createdAt().truncatedTo(ChronoUnit.MILLIS));
                                    }));
                });
    }



    private Stream<CustomerRequest> getStreamOfCustomerRequests() {
        return Stream.generate(() -> {
            Superhero superhero = faker.superhero();

            Address address = faker.address();
            return new CustomerRequest(
                    superhero.name(),
                    faker.name().lastName(),
                    new Email(faker.internet().emailAddress()),
                    new AddressDto(
                            AddressDto.AddressType.HOME,
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

    private @NonNull Consumer<HttpHeaders> getRequestHeaders(boolean asyncProcessingEnabled) {
        return headers -> {
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add(environment.getRequiredProperty("async.request.processing.header"), String.valueOf(asyncProcessingEnabled));
        };
    }
}
