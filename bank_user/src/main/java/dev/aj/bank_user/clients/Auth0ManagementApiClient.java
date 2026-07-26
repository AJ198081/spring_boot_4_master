package dev.aj.bank_user.clients;

import dev.aj.commons.rest.client.AbstractServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class Auth0ManagementApiClient extends AbstractServiceClient {

    @Override
    public String getServiceName() {
        return "Auth0-Management-Client";
    }

}
