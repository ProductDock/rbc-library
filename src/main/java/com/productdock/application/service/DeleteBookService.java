package com.productdock.application.service;

import com.productdock.adapter.in.web.dto.BookRentalStateDto;
import com.productdock.application.port.in.DeleteBookUseCase;
import com.productdock.application.port.out.messaging.DeleteBookMessagingOutPort;
import com.productdock.application.port.out.persistence.BookPersistenceOutPort;
import com.productdock.application.port.out.web.RentalsClient;
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
            throw new DeleteBookException("Book not found.");
        }
        var bookRentals = rentalsClient.getRentals(bookId);
        if (!bookRentals.isEmpty()) {
            throw new DeleteBookException("Book is currently in use by " + bookRentals.stream().findFirst().get().user().fullName());
        }
        bookRepository.deleteById(bookId);
        deleteBookMessagingOutPort.sendMessage(bookId);
        log.debug("deleted book with id: {}", bookId);
    }
}
