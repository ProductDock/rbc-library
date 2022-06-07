package com.productdock.book;

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
        reviewDto.userId = ((Jwt) authentication.getCredentials()).getClaim("email");
        reviewDto.userFullName = ((Jwt) authentication.getCredentials()).getClaim("name");
        reviewService.saveReview(reviewDto);
    }

}
