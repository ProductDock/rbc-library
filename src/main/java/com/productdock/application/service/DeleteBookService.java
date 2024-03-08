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

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteBookService implements DeleteBookUseCase {

    private final BookPersistenceOutPort bookRepository;

    private final DeleteBookMessagingOutPort deleteBookMessagingOutPort;

    private final RentalsClient rentalsClient;

    @Override
    @SneakyThrows
    public void deleteBook(Long bookId) {

        if (bookRepository.findById(bookId).isEmpty()) {
            throw new BookNotFoundException("Book not found.");
        }
        var bookRentals = rentalsClient.getRentals(bookId);
        if (!bookRentals.isEmpty()) {
            throw new DeleteBookException(createRentalMessage(bookRentals.stream().findFirst().get()));
        }
        bookRepository.deleteById(bookId);
        deleteBookMessagingOutPort.sendMessage(bookId);
        log.debug("deleted book with id: {}", bookId);
    }

    private String createRentalMessage(BookRentalStateDto bookRentals){
        if(bookRentals.status() == null || bookRentals.user() == null){
            return "Cannot read rental status.";
        }
        String status = bookRentals.status().toString().toLowerCase();
        String userName = bookRentals.user().fullName();
        return "Book is " + status + " by " + userName + ".";
    }
}
