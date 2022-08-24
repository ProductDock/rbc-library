package com.productdock.adapter.in.web;


import com.productdock.adapter.in.web.mapper.BookDtoMapper;
import com.productdock.application.port.in.GetBookQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/catalog/books")
record GetBookApi(GetBookQuery getBookQuery, BookDtoMapper bookDtoMapper) {

    @GetMapping("/{bookId}")
    public BookDto getBook(@PathVariable("bookId") Long bookId) {
        log.debug("GET request received - api/catalog/books/{}", bookId);
        var book = getBookQuery.getById(bookId);
        return bookDtoMapper.toDto(book);
    }

    @GetMapping
    public BookDto getBook(@RequestParam String title, @RequestParam String author) {
        log.debug("GET request received - api/catalog/books?title={}&author={}", title, author);
        var book = getBookQuery.getByTitleAndAuthor(title, author);
        return bookDtoMapper.toDto(book);
    }
}
