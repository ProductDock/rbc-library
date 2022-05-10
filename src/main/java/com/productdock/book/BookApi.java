package com.productdock.book;

import org.springframework.web.bind.annotation.*;

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
    public ReviewDto createReviewForBook(
            @PathVariable(value = "bookId", required = false) final Long bookId,
            @RequestBody ReviewDto reviewDto) {
        return reviewService.saveReview(reviewDto);
    }

}
