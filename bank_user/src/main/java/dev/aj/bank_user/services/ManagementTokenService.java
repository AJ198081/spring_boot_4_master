package dev.aj.bank_user.services;

import dev.aj.commons.types.TokenResponse;

public interface ManagementTokenService {

    String getBearerToken();

    TokenResponse getManagementToken();
}
