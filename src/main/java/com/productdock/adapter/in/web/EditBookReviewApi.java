package com.productdock.adapter.in.web;

import com.productdock.adapter.in.web.mapper.ReviewDtoMapper;
import com.productdock.application.port.in.EditBookReviewUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/catalog/books")
record EditBookReviewApi(EditBookReviewUseCase editBookReviewUseCase, ReviewDtoMapper reviewMapper) {
    public static final String USER_EMAIL = "email";
    public static final String USER_FULL_NAME = "fullName";

    @PutMapping("/{bookId}/reviews/{userId}")
    public void editReviewForBook(
            @PathVariable("bookId") final Long bookId,
            @PathVariable("userId") final String userId,
            @Valid @RequestBody ReviewDto reviewDto,
            Authentication authentication) {
        log.debug("PUT request received - api/catalog/books/{}/reviews/{}, Payload: {}", bookId, userId, reviewDto);
        String loggedUserEmail = ((Jwt) authentication.getCredentials()).getClaim(USER_EMAIL);

        if (!loggedUserEmail.equals(userId)) {
            log.warn("User with id:{}, tried to access forbidden resource [review] with id: [{},{}]", loggedUserEmail, bookId, userId);
            throw new AccessDeniedException("You don't have access for resource");
        }

        reviewDto.bookId = bookId;
        reviewDto.userId = loggedUserEmail;
        reviewDto.userFullName = ((Jwt) authentication.getCredentials()).getClaim(USER_FULL_NAME);
        var review = reviewMapper.toDomain(reviewDto);
        editBookReviewUseCase.editReview(review);
    }
}
