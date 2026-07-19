package dev.aj.security_oauth2_0.config;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@NullMarked
public class RestInterceptor implements ClientHttpRequestInterceptor {

    private final Environment environment;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            Jwt jwt = jwtAuthenticationToken.getToken();

            Object aud = jwt.getClaims().get("aud");

            if (aud instanceof List && ((List<?>) aud).contains(environment.getProperty("spring.security.oauth2.resourceserver.jwt.audiences"))) {

                request.getHeaders().add(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer ".concat(jwt.getTokenValue())
                );
            }

        }

        return execution.execute(request, body);
    }
}
