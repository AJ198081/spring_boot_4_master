package dev.aj.security_oauth2_0.conrollers;

import dev.aj.security_oauth2_0.util.HttpRequestInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringJUnitConfig(classes = {
        HttpRequestInterceptor.class
})
@TestPropertySource(locations = {
        "classpath:application.properties"
})
class RSVerifierIT {

    @Autowired
    private Environment environment;

    private RestTestClient restTestClient;

    @BeforeEach
    void setUp() {
        restTestClient = RestTestClient
                .bindToServer()
                .baseUrl("http://localhost:%s%s".formatted(
                        environment.getRequiredProperty("server.port"),
                        environment.getRequiredProperty("resource_server_uri")
                ))
                .build();
    }

    @Test
    void testPublicEndpoint() {
        restTestClient.get()
                .uri("/public")
                .exchange()
                .expectAll(
                        responseSpec -> {
                            responseSpec.expectStatus().is2xxSuccessful();

                            responseSpec.expectBody(String.class)
                                    .consumeWith(response -> {
                                                assert response.getResponseBody() != null;
                                                assertTrue(
                                                        response.getResponseBody().contains("welcome"));
                                            }
                                    );

                        }
                );
    }

    @Test
    void secureEndpoint() {
    }

    @Test
    void piiSecureEndpoint() {
    }
}