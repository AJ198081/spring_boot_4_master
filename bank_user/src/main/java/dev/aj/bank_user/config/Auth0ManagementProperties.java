package dev.aj.bank_user.config;

import dev.aj.commons.types.RequestBodyForToken;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "auth0.mgmt")
public record Auth0ManagementProperties(
        String audience,
        String clientId,
        String clientSecret,
        String tokenEndpoint,
        String grantType) {

    public RequestBodyForToken createTokenRequestBody() {
        return new RequestBodyForToken(clientId, clientSecret, audience, grantType);
    }
}
