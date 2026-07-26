package dev.aj.bank_user.services.impl;

import dev.aj.bank_user.clients.ManagementApiClient;
import dev.aj.bank_user.services.ManagementTokenService;
import dev.aj.commons.types.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class ManagementTokenServiceImpl implements ManagementTokenService {

    private final ManagementApiClient managementApiClient;

    @Override
    public String getBearerToken() {

        TokenResponse oAuthToken = managementApiClient.getOAuthToken();

        return oAuthToken.accessToken();
    }



}
