package com.productdock.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.interfaces.RSAPublicKey;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtAuthConverter jwtAuthConverter;
    @Value("${jwt.public.key}")
    RSAPublicKey key;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorize -> authorize.antMatchers("/actuator/**").permitAll()
                        .antMatchers("/api/catalog/books/*").hasRole("USER")
                        .anyRequest().authenticated())
                .cors().and()
                .oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthConverter);

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.key).build();
    }
}
