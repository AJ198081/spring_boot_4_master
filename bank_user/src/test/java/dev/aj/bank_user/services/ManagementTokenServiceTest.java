package dev.aj.bank_user.services;

import dev.aj.bank_user.clients.ManagementApiClient;
import dev.aj.bank_user.config.BankConfigs;
import dev.aj.bank_user.services.impl.ManagementTokenServiceImpl;
import org.assertj.core.api.Assertions;
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
}