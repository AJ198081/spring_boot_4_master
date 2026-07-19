package dev.aj.security_oauth2_0.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtToAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        Set<String> scopes = new LinkedHashSet<>();

//      Auth0 adds permissions in the generated JWT when RBAC is enabled, which we did for this application
        Object permissions = jwt.getClaims().get("permissions");

        if (permissions instanceof Collection<?> permissionsCollection) {
            scopes.addAll(permissionsCollection.stream()
                    .map(String::valueOf)
                    .collect(Collectors.toCollection(Collections::emptySet)));
        }

//      Scopes that we requested, and Auth0 added in the JWT
        String scopesInJwt = jwt.getClaimAsString("scope");

        if (!StringUtils.isBlank(scopesInJwt)) {
            scopes.addAll(Set.of(scopesInJwt.split(" ")));
        }

        List<SimpleGrantedAuthority> authorities = scopes.stream()
                .map("SCOPE_"::concat)
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new JwtAuthenticationToken(jwt, authorities);
    }

}
