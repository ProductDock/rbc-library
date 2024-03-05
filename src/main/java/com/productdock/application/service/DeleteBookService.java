package com.productdock.application.service;

import com.productdock.application.port.in.DeleteBookUseCase;
import com.productdock.application.port.out.persistence.BookPersistenceOutPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteBookService implements DeleteBookUseCase {

    private final BookPersistenceOutPort bookRepository;
    @Override
    public void deleteBook(Long bookId) {
        //TODO: implement checking availability

        if (bookRepository.findById(bookId).isPresent()){
            bookRepository.deleteById(bookId);
            log.info("deleted book with id: {}", bookId);
        }
    }
}
