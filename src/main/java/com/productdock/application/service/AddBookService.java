package com.productdock.application.service;

import com.productdock.application.port.in.AddBookUseCase;
import com.productdock.application.port.out.messaging.BookCatalogMessagingOutPort;
import com.productdock.application.port.out.persistence.BookPersistenceOutPort;
import com.productdock.domain.Book;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
class AddBookService implements AddBookUseCase {

    private BookPersistenceOutPort bookRepository;

    private BookCatalogMessagingOutPort bookCatalogMessagingOutPort;

    @Override
    @SneakyThrows
    public Long addBook(Book book, int bookCopies) {
        var insertedBook = bookRepository.save(book);
        bookCatalogMessagingOutPort.sendMessage(insertedBook, bookCopies);
        return insertedBook.getId();
    }

}
