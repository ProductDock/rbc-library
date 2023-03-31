package com.productdock.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.productdock.application.port.out.messaging.BookInventoryMessagingOutPort;
import com.productdock.application.port.out.messaging.BookMessagingOutPort;
import com.productdock.application.port.out.persistence.BookPersistenceOutPort;
import com.productdock.domain.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveBookServiceShould {

    @InjectMocks
    private SaveBookService saveBookService;

    @Mock
    private BookPersistenceOutPort bookRepository;

    @Mock
    private BookMessagingOutPort bookMessagingOutPort;

    @Mock
    private BookInventoryMessagingOutPort bookInventoryMessagingOutPort;

    @Test
    void saveBook() throws ExecutionException, InterruptedException, JsonProcessingException {
        var book = mock(Book.class);
        var insertedBook = mock(Book.class);
        int bookCopies = 1;
        when(bookRepository.save(book)).thenReturn(insertedBook);

        var bookId = saveBookService.saveBook(book, bookCopies);

        verify(bookRepository).save(book);
        verify(bookMessagingOutPort).sendMessage(insertedBook, bookCopies);
        verify(bookInventoryMessagingOutPort).sendMessage(insertedBook.getId(), bookCopies);
        assertThat(bookId).isEqualTo(insertedBook.getId());

    }
}
