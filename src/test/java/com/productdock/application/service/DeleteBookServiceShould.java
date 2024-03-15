package com.productdock.application.service;

import com.productdock.application.port.out.messaging.DeleteBookMessagingOutPort;
import com.productdock.application.port.out.persistence.BookPersistenceOutPort;
import com.productdock.application.port.out.web.RentalsClient;
import com.productdock.domain.Book;
import com.productdock.domain.BookRentalState;
import com.productdock.domain.RentalStatus;
import com.productdock.domain.UserProfile;
import com.productdock.domain.exception.BookNotFoundException;
import com.productdock.domain.exception.DeleteBookException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteBookServiceShould {

    private static final UserProfile USER_PROFILE = new UserProfile("Mocked name", null, null);
    private static final BookRentalState RENTAL_STATE = new BookRentalState(USER_PROFILE, RentalStatus.RENTED, null);

    private static final String EXCEPTION_MESSAGE = "Book in use by: Mocked name (rented)";
    private static Collection<BookRentalState> RENTALS = new ArrayList<>();
    private static final Optional<Book> BOOK = Optional.of(mock(Book.class));
    private static final Long BOOK_ID = 1L;

    @InjectMocks
    private DeleteBookService deleteBookService;
    @Mock
    private BookPersistenceOutPort bookRepository;
    @Mock
    private RentalsClient rentalsClient;
    @Mock
    private DeleteBookMessagingOutPort deleteBookMessagingOutPort;

    @Test
    void deleteBookWhenIdExist() {

        given(bookRepository.findById(BOOK_ID)).willReturn(BOOK);

        deleteBookService.deleteBook(BOOK_ID);

        verify(bookRepository).deleteById(BOOK_ID);
    }

    @Test
    void throwExceptionWhenBookDoesntExist() {
        given(bookRepository.findById(BOOK_ID)).willReturn(Optional.empty());

        assertThatThrownBy(() -> deleteBookService.deleteBook(BOOK_ID))
                .isInstanceOf(BookNotFoundException.class);
    }

    @Test
    void throwExceptionWhenBookIsTaken() throws IOException, InterruptedException {
        RENTALS.add(RENTAL_STATE);

        given(bookRepository.findById(BOOK_ID)).willReturn(BOOK);

        given(rentalsClient.getRentals(BOOK_ID)).willReturn(RENTALS);

        assertThatThrownBy(() -> deleteBookService.deleteBook(BOOK_ID))
                .isInstanceOf(DeleteBookException.class)
                .hasMessage(EXCEPTION_MESSAGE);
    }
}
