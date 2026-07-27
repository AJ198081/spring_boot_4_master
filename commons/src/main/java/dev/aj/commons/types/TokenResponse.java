package dev.aj.commons.types;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public record TokenResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("scope") String scope,
        @JsonProperty("expires_in") Long expiresIn,
        @JsonProperty("token_type") String tokenType) implements Serializable {
}

