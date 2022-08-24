package com.productdock.application.service;

import com.productdock.application.port.out.persistence.BookPersistenceOutPort;
import com.productdock.domain.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class GetBookServiceShould {

    private static final Optional<Book> BOOK = Optional.of(mock(Book.class));
    private static final Long BOOK_ID = 1L;
    private static final String BOOK_TITLE = "::title::";
    private static final String BOOK_AUTHOR = "::author::";

    @InjectMocks
    private GetBookService getBookService;

    @Mock
    private BookPersistenceOutPort bookRepository;

    @Test
    void getBookById_whenIdExist() {
        given(bookRepository.findById(BOOK_ID)).willReturn(BOOK);

        var book = getBookService.getById(BOOK_ID);

        assertThat(book).isEqualTo(BOOK.get());
    }

    @Test
    void throwException_whenIdNotExist() {
        given(bookRepository.findById(BOOK_ID)).willReturn(Optional.empty());

        assertThatThrownBy(() -> getBookService.getById(BOOK_ID))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void getBookByTitleAndAuthor_whenExist() {
        given(bookRepository.findByTitleAndAuthor(BOOK_TITLE, BOOK_AUTHOR)).willReturn(BOOK);

        var book = getBookService.getByTitleAndAuthor(BOOK_TITLE, BOOK_AUTHOR);

        assertThat(book).isEqualTo(BOOK.get());
    }

    @Test
    void throwException_whenBookWithTitleAndAuthorNotExist() {
        given(bookRepository.findByTitleAndAuthor(BOOK_TITLE, BOOK_AUTHOR)).willReturn(Optional.empty());

        assertThatThrownBy(() -> getBookService.getByTitleAndAuthor(BOOK_TITLE, BOOK_AUTHOR))
                .isInstanceOf(NoSuchElementException.class);
    }
}
