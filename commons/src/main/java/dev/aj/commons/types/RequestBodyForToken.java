package dev.aj.commons.types;

public record RequestBodyForToken(String client_id, String client_secret, String audience, String grant_type) {
}
