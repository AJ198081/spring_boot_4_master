package dev.aj.bank_user.clients;

import dev.aj.bank_user.config.BankConfigs;
import dev.aj.bank_user.config.ComposeConfigurations;
import dev.aj.commons.types.TokenResponse;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringJUnitConfig(classes = {
        ComposeConfigurations.class,
        ManagementApiClient.class,
        BankConfigs.class
}
)
@TestPropertySource(locations = {
        "classpath:application.properties",
        "classpath:application-test.properties",
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ManagementApiClientTest {

    @Autowired
    private ManagementApiClient managementApiClient;

    @RepeatedTest(10)
    void getOAuthToken() {

        TokenResponse oAuthToken = managementApiClient.getOAuthToken();
        assertNotNull(oAuthToken);
    }
}