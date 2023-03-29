package com.productdock.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class JwtAuthConverterShould {

    private final JwtAuthConverter jwtAuthConverter = new JwtAuthConverter();

    private static final String DEFAULT_ROLE = "ROLE_USER";
    @Test
    void extractGrantedAuthoritiesFromJwt() {
        var jwtToken = mock(Jwt.class);

        given(jwtToken.hasClaim("scope")).willReturn(true);
        given(jwtToken.getClaim("scope")).willReturn(DEFAULT_ROLE);

        var abstractAuthenticationToken = jwtAuthConverter.convert(jwtToken);


        assertThat(abstractAuthenticationToken).isNotNull();
        assertThat(abstractAuthenticationToken.getAuthorities())
                .extracting("role")
                .containsExactlyInAnyOrder(DEFAULT_ROLE);
    }
}
