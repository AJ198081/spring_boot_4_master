package dev.aj.order_service.controller;

import dev.aj.order_service.model.order.OrderRequest;
import dev.aj.order_service.model.order.OrderResponse;
import net.datafaker.Faker;
import net.datafaker.service.Range;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;

import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles(value = {"test", "local"})
class OrderControllerTest {

    private RestClient restClient;

    @Autowired
    private Environment environment;

    @Autowired
    private Faker faker;

    @BeforeEach
    void setUp() {

        restClient = RestClient.builder()
                .baseUrl("http://localhost:%s".formatted(environment.getProperty("server.port")))
                .defaultHeaders(httpHeaders -> {
                            httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
                            httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                        }
                )
                .build();

    }

    @Test
    void placeOrder() {

        Stream<OrderRequest> orderRequests = this.getStreamOfOrderRequests();

        OrderResponse placedOrderResponse = restClient.post()
                .uri("%s".formatted(environment.getProperty("order_service.uri")))
                .body(orderRequests.limit(1).findAny().orElseThrow())
                .retrieve()
                .body(OrderResponse.class);
        System.out.println(placedOrderResponse);

        assertThat(placedOrderResponse)
                .isNotNull();

    }

    private Stream<OrderRequest> getStreamOfOrderRequests() {
        return Stream.generate(() -> new OrderRequest(
                UUID.randomUUID(),
                faker.commerce().productName(),
                faker.random().nextInt(Range.inclusive(1,10))
        ));
    }

    @Test
    void cancelOrder() {
    }
}