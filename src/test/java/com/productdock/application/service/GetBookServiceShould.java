package com.productdock.application.service;

import com.productdock.application.port.out.persistence.BookPersistenceOutPort;
import com.productdock.domain.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class GetBookServiceShould {

    private static final Optional<Book> BOOK = Optional.of(mock(Book.class));
    private static final Long BOOK_ID = 1L;

    @InjectMocks
    private GetBookService getBookService;

    @Mock
    private BookPersistenceOutPort bookRepository;

    @Test
    void getBookById(){
        given(bookRepository.findById(BOOK_ID)).willReturn(BOOK);

        var book = getBookService.getById(BOOK_ID);

        assertThat(book).isInstanceOf(Book.class);
    }

}
