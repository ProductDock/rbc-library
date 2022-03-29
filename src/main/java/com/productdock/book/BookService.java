package com.productdock.book;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public record BookService(BookRepository bookRepository,
                          BookMapper bookMapper) {

    public List<BookDto> getAll(int pageNumber) {
        Pageable firstPage = PageRequest.of(pageNumber, 18);
        return bookRepository
                .findAll(firstPage)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    public long countAllBooks() {
        return bookRepository.count();
    }
}
