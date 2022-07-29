package com.productdock.config;

import com.productdock.library.jwt.validator.JwtTokenFilter;
import com.productdock.library.jwt.validator.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Bean
    public JwtTokenProvider jwtTokenProvider()  {
        return new JwtTokenProvider(secretKey);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .anyRequest().authenticated().and()

                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider()),
                        BasicAuthenticationFilter.class);
        http.csrf().disable();

    }
}
