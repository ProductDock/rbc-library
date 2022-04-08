package com.productdock.book;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public record BookService(BookRepository bookRepository,
                          BookMapper bookMapper) {

    private static final int PAGE_SIZE = 18;

    public List<BookDto> getBooks(List<String> topics, int page) {
        if (isEmpty(topics)) {
            return getAllBooks(page);
        }
        return getBooksByTopics(topics, page);
    }

    private List<BookDto> getAllBooks(int page) {
        var pageTemplate = PageRequest.of(page, PAGE_SIZE);
        return bookRepository
                .findAll(pageTemplate)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    private List<BookDto> getBooksByTopics(List<String> topics, int page) {
        var pageTemplate = PageRequest.of(page, PAGE_SIZE);
        return bookRepository
                .findAllByTopicsName(topics, pageTemplate)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    public long countAllBooks() {
        return bookRepository.count();
    }
}
