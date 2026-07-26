package dev.aj.bank_user.clients;

import dev.aj.bank_user.config.Auth0ManagementProperties;
import dev.aj.commons.rest.client.AbstractServiceClient;
import dev.aj.commons.types.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManagementApiClient extends AbstractServiceClient {

    private final Auth0ManagementProperties properties;

    @Override
    protected String getServiceName() {
        return "auth0-management-api";
    }


    public TokenResponse getOAuthToken() {

        RestClient.ResponseSpec responseSpec = this.executeRequest(() -> getManagementRestClient()
                .post()
                .body(properties.createTokenRequestBody())
                .retrieve());

        return responseSpec.body(TokenResponse.class);
    }

    private RestClient getManagementRestClient() {
        return createRestClient(properties.tokenEndpoint());
    }
}
