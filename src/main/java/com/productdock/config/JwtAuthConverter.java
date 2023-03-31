package com.productdock.config;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        var grantedAuthorities = jwtGrantedAuthoritiesConverter.convert(jwt);
        if (grantedAuthorities == null) {
            grantedAuthorities = new ArrayList<>();
        }
        return new JwtAuthenticationToken(jwt, grantedAuthorities.stream().toList(), getIssuerName(jwt));
    }

    private String getIssuerName(Jwt jwt) {
        return jwt.getClaim(JwtClaimNames.ISS);
    }
}
