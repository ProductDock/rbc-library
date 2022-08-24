package com.productdock.application.service;

import com.productdock.application.port.in.GetBookQuery;
import com.productdock.application.port.out.persistence.BookPersistenceOutPort;
import com.productdock.domain.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
record GetBookService(BookPersistenceOutPort bookRepository) implements GetBookQuery {

    @Override
    public Book getById(Long bookId) {
        log.debug("Fetched book with book id: {}", bookId);
        return bookRepository.findById(bookId).orElseThrow();
    }

    @Override
    public Book getByTitleAndAuthor(String title, String author) {
        log.debug("Fetched book with title: {} and author: {}", title, author);
        return bookRepository.findByTitleAndAuthor(title, author).orElseThrow();
    }
}
