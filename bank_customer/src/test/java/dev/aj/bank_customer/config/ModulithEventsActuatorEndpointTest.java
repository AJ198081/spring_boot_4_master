package dev.aj.bank_customer.config;

import dev.aj.bank_customer.bootstrap.Config;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.Map;
import java.util.UUID;

@SpringJUnitConfig(classes = {
        ModulithEventsActuatorEndpointTest.class
        , Config.class}
)
@TestPropertySource(value = {
        "classpath:application.properties"
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ModulithEventsActuatorEndpointTest {

    private RestTestClient restTestClient;

    @Autowired
    private Environment environment;

    @BeforeAll
    void setUp() {
        restTestClient = RestTestClient.bindToServer()
                .baseUrl("http://localhost:%s%s"
                        .formatted(
                                environment.getRequiredProperty("management.server.port"),
                                environment.getRequiredProperty("management.endpoints.web.base-path"))
                )
                .build();

    }

    @Test
    void retriggerIncompleteEvents() {
        restTestClient.get()
                .uri("/trigger-events")
                .exchange()
                .expectBody(new ParameterizedTypeReference<Map<String, String>>() {
                })
                .consumeWith(response -> Assertions.assertThat(response.getResponseBody())
                        .isNotNull()
                        .isNotEmpty());
    }

    @Test
    void triggerIndividualEvent() {

        UUID eventIdentifier = UUID.fromString("e3c8efb8-bd4f-4973-9f58-76d1c5e13742");

        restTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("/trigger-events/{eventIdentifier}")
                        .build(eventIdentifier))
                .exchange()
                .expectBody(String.class)
                .consumeWith(response -> {
                    response.getStatus().is2xxSuccessful();
                    Assertions.assertThat(response.getResponseBody())
                            .isNotNull()
                            .contains(eventIdentifier.toString());
                });
    }
}
