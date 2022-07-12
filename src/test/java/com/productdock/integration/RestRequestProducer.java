package com.productdock.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
class RestRequestProducer {

    public static final String DEFAULT_USER_ID = "::userId::";

    @Autowired
    private MockMvc mockMvc;

    public ResultActions makeEditBookReviewRequest(String reviewDtoJson, Long bookId, String userId) throws Exception {
        return mockMvc.perform(put("/api/catalog/books/" + bookId + "/reviews/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reviewDtoJson)
                .with(jwt().jwt(jwt -> {
                    jwt.claim("email", DEFAULT_USER_ID);
                    jwt.claim("name", "::userFullName::");
                })));
    }

    public ResultActions makeGetBookRequest(Long bookId) throws Exception {
        return mockMvc.perform(get("/api/catalog/books/" + bookId)
                        .with(jwt().jwt(jwt -> {
                            jwt.claim("email", DEFAULT_USER_ID);
                            jwt.claim("name", "::userFullName::");
                        })))
                .andExpect(status().isOk());
    }

    public ResultActions makeBookReviewRequest(String reviewDtoJson, Long bookId) throws Exception {
        return mockMvc.perform(post("/api/catalog/books/" + bookId + "/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reviewDtoJson)
                .with(jwt().jwt(jwt -> {
                    jwt.claim("email", DEFAULT_USER_ID);
                    jwt.claim("name", "::userFullName::");
                })));
    }

    public ResultActions makeDeleteBookReviewRequest(Long bookId, String userId) throws Exception {
        return mockMvc.perform(delete("/api/catalog/books/" + bookId + "/reviews/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt().jwt(jwt -> {
                    jwt.claim("email", DEFAULT_USER_ID);
                    jwt.claim("name", "::userFullName::");
                })));
    }
}
