package dev.aj.bank_user.services.impl;

import dev.aj.bank_user.config.Auth0ManagementProperties;
import dev.aj.bank_user.config.ComposeConfigurations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.client.RestTestClient;


@SpringJUnitConfig(classes = {
        ComposeConfigurations.class}
)
@TestPropertySource(locations = {
        "classpath:/application.properties",
        "classpath:application-test.properties"
})
class ManagementTokenServiceImplIT {

    private RestTestClient restClient;

    @Autowired
    Auth0ManagementProperties auth0ManagementProperties;

    @BeforeEach
    void setUp() {
        restClient = RestTestClient.bindToServer()
                .baseUrl(auth0ManagementProperties.tokenEndpoint())
                .build();

    }

    @RepeatedTest(value = 10)
    void getManagementToken() {

        restClient.post()
                .body(auth0ManagementProperties.createTokenRequestBody())
                .exchange()
                .expectStatus()
                .isOk();
    }
}
