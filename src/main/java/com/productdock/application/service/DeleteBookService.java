package com.productdock.application.service;

import com.productdock.adapter.in.web.dto.BookRentalStateDto;
import com.productdock.application.port.in.DeleteBookUseCase;
import com.productdock.application.port.out.messaging.DeleteBookMessagingOutPort;
import com.productdock.application.port.out.persistence.BookPersistenceOutPort;
import com.productdock.application.port.out.web.RentalsClient;
import com.productdock.domain.exception.BookNotFoundException;
import com.productdock.domain.exception.DeleteBookException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
class DeleteBookService implements DeleteBookUseCase {

    private final BookPersistenceOutPort bookRepository;

    private final DeleteBookMessagingOutPort deleteBookMessagingOutPort;

    private final RentalsClient rentalsClient;

    @Override
    @SneakyThrows
    public void deleteBook(Long bookId) {
        validate(bookId);
        bookRepository.deleteById(bookId);
        deleteBookMessagingOutPort.sendMessage(bookId);
        log.debug("deleted book with id: {}", bookId);
    }

    @SneakyThrows
    private void validate(Long bookId) {
        bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("Book not found."));

        var bookRentals = rentalsClient.getRentals(bookId);
        if (!bookRentals.isEmpty()) {
            throw new DeleteBookException(createRentalMessage(bookRentals));
        }
    }

    private String createRentalMessage(Collection<BookRentalStateDto> bookRentals) {
        var message = "Book is ";
        for (var rental : bookRentals) {
            if (rental.status() == null || rental.user() == null) {
                return "Cannot read rental status.";
            }
            var status = rental.status().toString().toLowerCase();
            var userName = rental.user().fullName();

            message = message.concat(status).concat(" by ").concat(userName).concat(". ");
        }

        return message;
    }
}
