package dev.aj.bank_user.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringJUnitConfig
@TestPropertySource(locations = {
        "classpath:application.properties",
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest {

    @Autowired
    private Environment environment;

    private RestTestClient restTestClient;

    @BeforeAll
    void setUp() {

        restTestClient = RestTestClient.bindToServer()
                .baseUrl("http://localhost:%s%s".formatted(
                        environment.getRequiredProperty("server.port"),
                        environment.getRequiredProperty("bank_user_uri")
                ))
                .build();

    }

    @Test
    void testGetPublicEndpoint() {

        RestTestClient.BodySpec<String, ?> expectedBody = restTestClient.get()
                .uri("/public")
                .exchange()
                .expectBody(String.class);

        expectedBody.consumeWith(responseEntity -> {
            assert responseEntity.getResponseBody() != null;
            assertTrue(responseEntity.getResponseBody().contains("welcome"));
        });
    }


    @Test
    void testGetSecureEndpoint() {

        RestTestClient.BodySpec<String, ?> expectedBody = restTestClient.get()
                .uri("/secure")
                .exchange()
                .expectBody(String.class);

        expectedBody.consumeWith(responseEntity -> {
            assert responseEntity.getResponseBody() != null;
            assertTrue(responseEntity.getResponseBody().contains("welcome"));
        });
    }
}