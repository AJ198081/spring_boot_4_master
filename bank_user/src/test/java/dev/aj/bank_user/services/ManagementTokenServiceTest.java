package dev.aj.bank_user.services;

import dev.aj.bank_user.clients.ManagementApiClient;
import dev.aj.bank_user.config.BankConfigs;
import dev.aj.bank_user.services.impl.ManagementTokenServiceImpl;
import dev.aj.commons.types.TokenResponse;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(
        classes = {
                BankConfigs.class,
                ManagementTokenServiceImpl.class,
                ManagementApiClient.class,
        }
)
@TestPropertySource(locations = {
        "classpath:application.properties",
        "classpath:application-test.properties"
})
class ManagementTokenServiceTest {

    @Autowired
    ManagementTokenService managementTokenService;

    @Test
    void getBearerToken() {
        String bearerToken = managementTokenService.getBearerToken();

        Assertions.assertThat(bearerToken)
                .isNotBlank();
    }

    @Test
    void getManagementToken() {
        TokenResponse tokenResponse = managementTokenService.getManagementToken();

        Assertions.assertThat(tokenResponse)
                .isNotNull()
                .extracting(TokenResponse::accessToken, TokenResponse::expiresIn)
                .satisfies(response -> {

                    Assertions.assertThat(response.getFirst())
                            .isNotNull()
                            .asInstanceOf(InstanceOfAssertFactories.STRING)
                            .isNotBlank()
                            .hasSizeGreaterThanOrEqualTo(1);

                    Assertions.assertThat(response.getLast())
                            .asInstanceOf(InstanceOfAssertFactories.LONG)
                            .isLessThanOrEqualTo(87400L);
                });

    }
}