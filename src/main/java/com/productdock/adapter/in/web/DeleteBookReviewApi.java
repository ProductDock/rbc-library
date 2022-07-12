package com.productdock.adapter.in.web;

import com.productdock.application.port.in.DeleteBookReviewUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/catalog/books")
record DeleteBookReviewApi(DeleteBookReviewUseCase deleteBookReviewUseCase) {

    public static final String USER_EMAIL = "email";

    @DeleteMapping("/{bookId}/reviews/{userId}")
    public void deleteReviewForBook(
            @PathVariable("bookId") final Long bookId,
            @PathVariable("userId") final String userId,
            Authentication authentication) {
        log.debug("DELETE request received - api/catalog/books/{}/reviews/{}", bookId, userId);
        String loggedUserEmail = ((Jwt) authentication.getCredentials()).getClaim(USER_EMAIL);

        if (!loggedUserEmail.equals(userId)) {
            log.warn("User with id:{}, tried to access forbidden resource [review] with id: [{},{}]", loggedUserEmail, bookId, userId);
            throw new AccessDeniedException("You don't have access for resource");
        }

        deleteBookReviewUseCase.deleteReview(bookId, userId);
    }
}
