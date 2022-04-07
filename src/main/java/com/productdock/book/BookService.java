package com.productdock.book;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public record BookService(BookRepository bookRepository,
                          BookMapper bookMapper) {

    private static final int PAGE_SIZE = 18;

    public List<BookDto> getBooks(List<String> topics, int page) {
        if(CollectionUtils.isEmpty(topics)){
            return getAllBooks(page);
        }
        return getBooksByTopics(topics, page);
    }

    private List<BookDto> getAllBooks(int page){
        var pageTemplate = PageRequest.of(page, PAGE_SIZE);
        return bookRepository
                .findAll(pageTemplate)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    private List<BookDto> getBooksByTopics(List<String> topics, int page){
        var pageTemplate = PageRequest.of(page, PAGE_SIZE);
        return bookRepository
                .findAllByTopics_Name(topics, pageTemplate)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    public long countAllBooks() {
        return bookRepository.count();
    }
}
