package com.productdock.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookApi {

    private final BookService bookService;

    private final BookMapper bookMapper;

    public BookApi(BookService bookService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @GetMapping
    public ResponseEntity<List<BookDto>> getAll() {
        List<BookDto> bookDtos = bookService.getAll()
                .stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(bookDtos, HttpStatus.OK);
    }
}
