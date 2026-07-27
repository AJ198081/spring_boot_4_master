package dev.aj.bank_user.services.impl;

import dev.aj.bank_user.model.dtos.CreateUser;
import dev.aj.bank_user.model.dtos.UserCreated;
import dev.aj.bank_user.services.Auth0UserService;
import dev.aj.bank_user.services.ManagementTokenService;
import dev.aj.commons.types.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class Auth0UserServiceImpl implements Auth0UserService {

    private final ManagementTokenService managementTokenService;

    @Override
    public UserCreated createNewUser(CreateUser createUserRequest) {

        TokenResponse accessToken = managementTokenService.getManagementToken();



        return null;
    }
}
