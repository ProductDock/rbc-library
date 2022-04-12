package com.productdock.book;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public record BookService(BookRepository bookRepository,
                          BookMapper bookMapper) {

    private static final int PAGE_SIZE = 18;

    public List<BookDto> getBooks(Optional<List<String>> topics, int page) {
        var pageTemplate = PageRequest.of(page, PAGE_SIZE);
        return bookRepository
                .findByTopicsName(topics, pageTemplate)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    public long countAllBooks() {
        return bookRepository.count();
    }
}
