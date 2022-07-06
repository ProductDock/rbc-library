package com.productdock.application.service;

import com.productdock.application.port.in.GetBookQuery;
import com.productdock.application.port.out.persistence.BookPersistenceOutPort;
import com.productdock.domain.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public record GetBookService(BookPersistenceOutPort bookRepository) implements GetBookQuery {

    public Book getById(Long bookId) {
        log.debug("Fetched book with book id: {}", bookId);
        return bookRepository.findById(bookId).orElseThrow();
    }

}
