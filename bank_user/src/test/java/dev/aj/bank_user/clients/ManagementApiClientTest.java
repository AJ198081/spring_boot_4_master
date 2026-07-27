package dev.aj.bank_user.clients;

import dev.aj.bank_user.config.CacheConfig;
import dev.aj.bank_user.config.ComposeConfigurations;
import dev.aj.commons.types.TokenResponse;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringJUnitConfig(classes = {
        ComposeConfigurations.class,
        ManagementApiClient.class,
        CacheConfig.class
})
@TestPropertySource(locations = {
        "classpath:application.properties",
        "classpath:application-test.properties",
})
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ManagementApiClientTest {

    private static final String AUTH_TOKEN_CACHE_KEY = "mgmt_api_cache::getOAuthToken";
    @Autowired
    private ManagementApiClient managementApiClient;

    @Test
    @Order(1)
    void testClearCacheWhenCacheIsNotPopulated() {

        managementApiClient.clearCache(AUTH_TOKEN_CACHE_KEY);

        Boolean clearedCache = managementApiClient.clearCache(AUTH_TOKEN_CACHE_KEY);

        assertNotNull(clearedCache);
        assertFalse(clearedCache);

    }

    @RepeatedTest(100)
    @Order(2)
    void getOAuthToken() {

        TokenResponse oAuthToken = managementApiClient.getOAuthToken();
        assertNotNull(oAuthToken);
    }

    @Test
    @Order(3)
    void testClearCacheWhenCacheIsPopulated() {
        managementApiClient.getOAuthToken();

        Boolean clearedCache = managementApiClient.clearCache(AUTH_TOKEN_CACHE_KEY);

        assertNotNull(clearedCache);
        assertTrue(clearedCache);

    }
}