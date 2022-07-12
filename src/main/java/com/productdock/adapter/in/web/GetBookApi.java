package com.productdock.adapter.in.web;


import com.productdock.adapter.in.web.mapper.BookDtoMapper;
import com.productdock.application.port.in.GetBookQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
