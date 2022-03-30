package com.productdock.book;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public record BookApi(BookService bookService) {

    @GetMapping
    public List<BookDto> getBooks(@RequestParam int page) {
        return bookService.getAll(page);
    }

    @GetMapping("count")
    public long countAllBooks() {
        return bookService.countAllBooks();
    }
}
