package dev.aj.bank_user.clients;

import dev.aj.bank_user.config.Auth0ManagementProperties;
import dev.aj.commons.rest.client.AbstractServiceClient;
import dev.aj.commons.types.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Slf4j
@CacheConfig(cacheNames = "mgmt_api_cache")
public class ManagementApiClient extends AbstractServiceClient {

    private final Auth0ManagementProperties properties;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    protected String getServiceName() {
        return "auth0-management-api";
    }


    @Cacheable(key = "#root.methodName")
    public TokenResponse getOAuthToken() {

        RestClient.ResponseSpec responseSpec = this.executeRequest(() -> getManagementRestClient()
                .post()
                .body(properties.createTokenRequestBody())
                .retrieve());

        return responseSpec.body(TokenResponse.class);
    }

    public Boolean clearCache(String cacheKey) {
        return redisTemplate.delete(cacheKey);
    }



    private RestClient getManagementRestClient() {
        return createRestClient(properties.tokenEndpoint());
    }
}
