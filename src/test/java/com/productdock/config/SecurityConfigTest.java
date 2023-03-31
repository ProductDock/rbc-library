package com.productdock.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"mock-db"})
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String ROLE_USER = "ROLE_USER";

    @Test
    void givenUnauthenticated_thenUnauthorizedResponse() throws Exception {
        mockMvc.perform(get("/api/catalog/books/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void givenUnauthorized_thenForbiddenResponse() throws Exception {
        mockMvc.perform(post("/api/catalog/books")
                        .with(jwt().jwt(jwt -> {
                            jwt.claim("scope", ROLE_USER);
                        })))
                .andExpect(status().isForbidden());
    }
}
