package com.productdock.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.interfaces.RSAPublicKey;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    @Value("${jwt.public.key}")
    RSAPublicKey key;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorize -> authorize.antMatchers("/actuator/**").permitAll()
                        .antMatchers(HttpMethod.POST, "/api/catalog/books").hasAuthority("SCOPE_ROLE_ADMIN")
                        .antMatchers(HttpMethod.DELETE, "/api/catalog/books/{bookId}").hasAuthority("SCOPE_ROLE_ADMIN")
                        .anyRequest().authenticated())
                .cors().and()
                .oauth2ResourceServer().jwt();

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.key).build();
    }
}
