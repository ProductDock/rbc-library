package com.productdock.book;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/catalog/books")
public record BookApi(BookService bookService, ReviewService reviewService) {

    @GetMapping
    public SearchBooksResponse getBooks(@RequestParam(required = false) Optional<List<String>> topics, @RequestParam int page) {
        return bookService.getBooks(topics, page);
    }

    @GetMapping("/{bookId}")
    public BookDto getBook(@PathVariable("bookId") Long bookId) {
        return bookService.findById(bookId);
    }

    @PostMapping("/{bookId}/reviews")
    public void createReviewForBook(
            @PathVariable("bookId") final Long bookId,
            @Valid @RequestBody ReviewDto reviewDto,
            Authentication authentication) {
        reviewDto.bookId = bookId;
        reviewDto.userId = ((Jwt) authentication.getCredentials()).getClaim("email");
        reviewDto.userFullName = ((Jwt) authentication.getCredentials()).getClaim("name");
        reviewService.saveReview(reviewDto);
    }

}
