package com.productdock.book;

import com.productdock.exception.ForbiddenAccessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/catalog/books")
public record BookApi(BookService bookService, ReviewService reviewService) {

    public static final String USER_EMAIL = "email";
    public static final String USER_NAME = "name";

    @GetMapping
    public SearchBooksResponse getBooks(@RequestParam(required = false) Optional<List<String>> topics, @RequestParam int page) {
        log.debug("GET request received - api/catalog/books");
        return bookService.getBooks(topics, page);
    }

    @GetMapping("/{bookId}")
    public BookDto getBook(@PathVariable("bookId") Long bookId) {
        log.debug("GET request received - api/catalog/books/{}", bookId);
        return bookService.findById(bookId);
    }

    @PostMapping("/{bookId}/reviews")
    public void createReviewForBook(
            @PathVariable("bookId") final Long bookId,
            @Valid @RequestBody ReviewDto reviewDto,
            Authentication authentication) {
        log.debug("POST request received - api/catalog/books/{}/reviews, Payload: {}", bookId, reviewDto);
        reviewDto.bookId = bookId;
        reviewDto.userId = ((Jwt) authentication.getCredentials()).getClaim(USER_EMAIL);
        reviewDto.userFullName = ((Jwt) authentication.getCredentials()).getClaim(USER_NAME);
        reviewService.saveReview(reviewDto);
    }

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
            throw new ForbiddenAccessException("You don't have access for resource");
        }

        reviewDto.bookId = bookId;
        reviewDto.userId = loggedUserEmail;
        reviewDto.userFullName = ((Jwt) authentication.getCredentials()).getClaim(USER_NAME);
        reviewService.editReview(reviewDto);
    }

    @DeleteMapping("/{bookId}/reviews")
    public void deleteReviewForBook(
            @RequestParam("k_book") final Long bookId,
            @RequestParam("k_user") final String userId,
            Authentication authentication) {
        log.debug("DELETE request received - api/catalog/books/{}/reviews?k_book={}&k_user={}", bookId, bookId, userId);
        String loggedUserEmail = ((Jwt) authentication.getCredentials()).getClaim(USER_EMAIL);

        if (!loggedUserEmail.equals(userId)) {
            log.warn("User with id:{}, tried to access forbidden resource [review] with id: [{},{}]", loggedUserEmail, bookId, userId);
            throw new ForbiddenAccessException("You don't have access for resource");
        }

        reviewService.deleteReview(bookId, userId);
    }
}
