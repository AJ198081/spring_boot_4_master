package dev.aj.bank_user.clients;

import dev.aj.bank_user.config.ComposeConfigurations;
import dev.aj.commons.types.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringJUnitConfig(classes = {
        ComposeConfigurations.class,
        ManagementApiClient.class
}
)
@TestPropertySource(locations = {
        "classpath:/application.properties",
})
class ManagementApiClientTest {

    @Autowired
    private ManagementApiClient managementApiClient;

    @BeforeEach
    void setUp() {
    }

    @RepeatedTest(10)
    void getOAuthToken() {

        TokenResponse oAuthToken = managementApiClient.getOAuthToken();
        assertNotNull(oAuthToken);
    }
}