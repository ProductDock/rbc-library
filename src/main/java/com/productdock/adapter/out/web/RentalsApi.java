package com.productdock.adapter.out.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.productdock.adapter.in.web.dto.BookRentalStateDto;
import com.productdock.application.port.out.web.RentalsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
public class RentalsApi implements RentalsClient {

    private String rentalsServiceUrl;
    private HttpClient client = HttpClient.newHttpClient();

    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    public RentalsApi(@Value("http://localhost:8083/api/rental/book/") String rentalsServiceUrl) {
        this.rentalsServiceUrl = rentalsServiceUrl;
    }

    @Override
    public Collection<BookRentalStateDto> getRentals(Long bookId) throws IOException, InterruptedException {
        var jwt = ((Jwt) SecurityContextHolder.getContext().getAuthentication().getCredentials()).getTokenValue();
        var uri = new DefaultUriBuilderFactory(rentalsServiceUrl)
                .builder()
                .path(bookId.toString())
                .path("/rentals")
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        log.debug("{}", response.body());
        return objectMapper.readValue(response.body(), new TypeReference<Collection<BookRentalStateDto>>() {
        });
    }
}
