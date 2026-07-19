package dev.aj.security_oauth2_0.config;

import dev.aj.security_oauth2_0.util.JwtToAuthConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class DefaultSecurityConfig {

    private final Environment environment;
    private final JwtToAuthConverter jwtToAuthConverter;

    @Bean
    public SecurityFilterChain rsSecurityFilterChain(HttpSecurity httpSecurity) {


        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(amRequestMatcher -> amRequestMatcher
                        .requestMatchers(
                                "/actuator/health",
                                "/api/v1/health",
                                "/api/v1/customer/register",
                                "/.well-known/jwks.json").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oAuth2RSConfigurer ->
                        oAuth2RSConfigurer
                                .jwt(jwtConfigurer -> jwtConfigurer
                                        .decoder(jwtDecoder())
                                        .jwtAuthenticationConverter(jwtToAuthConverter))
                )
                .build();
    }

    private JwtDecoder jwtDecoder() {

        String issuer = environment.getRequiredProperty("spring.security.oauth2.resourceserver.jwt.issuer-uri");

        // Configure the Nimbus implementation with 'fromIssuer' location, will use JWKS keys to discover the issuer from URL
        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(issuer);

        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);

        String audience = environment.getRequiredProperty("spring.security.oauth2.resourceserver.jwt.audiences");
        OAuth2TokenValidator<Jwt> withAudience = token -> {
            Object aud = token.getClaims().get("aud");
            if (aud instanceof List && ((List<?>) aud).contains(audience)) {
                return OAuth2TokenValidatorResult.success();
            }

            return OAuth2TokenValidatorResult.failure(new OAuth2Error(
                    HttpStatus.UNAUTHORIZED.toString(),
                    "Missing/Invalid audiences",
                    null)
            );
        };

        jwtDecoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(withIssuer, withAudience));

        return jwtDecoder;
    }


}
