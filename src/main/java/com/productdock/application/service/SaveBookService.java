package com.productdock.application.service;

import com.productdock.application.port.in.SaveBookUseCase;
import com.productdock.application.port.out.messaging.BookInventoryMessagingOutPort;
import com.productdock.application.port.out.messaging.BookMessagingOutPort;
import com.productdock.application.port.out.persistence.BookPersistenceOutPort;
import com.productdock.domain.Book;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
class SaveBookService implements SaveBookUseCase {

    private BookPersistenceOutPort bookRepository;

    private BookMessagingOutPort bookMessagingOutPort;

    private BookInventoryMessagingOutPort bookInventoryMessagingOutPort;

    @Override
    @SneakyThrows
    public Long saveBook(Book book, int bookCopies) {
        var insertedBook = bookRepository.save(book);
        bookMessagingOutPort.sendMessage(insertedBook, bookCopies);
        bookInventoryMessagingOutPort.sendMessage(insertedBook.getId(), bookCopies);
        return insertedBook.getId();
    }

}
