package com.productdock.book;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public record BookApi(BookService bookService) {

    @GetMapping
    public SearchBooksResponse getBooks(@RequestParam(required = false) Optional<List<String>> topics, @RequestParam int page) {
        return bookService.getBooks(topics, page);
    }

    @GetMapping("/{bookId}")
    public BookDto getBook(@PathVariable("bookId") Long bookId){
        return bookService.findById(bookId);
    }

}
