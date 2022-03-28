package com.productdock.book;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public record BookService(BookRepository bookRepository,
                          BookMapper bookMapper) {

    public List<BookDto> getAll() {
        return bookRepository
                .findAll()
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    public Integer countAllBooks() {
        return Math.toIntExact(bookRepository.count());
    }
}
