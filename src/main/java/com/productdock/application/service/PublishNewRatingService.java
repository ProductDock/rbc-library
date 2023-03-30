package com.productdock.application.service;

import com.productdock.application.port.out.messaging.BookRatingMessagingOutPort;
import com.productdock.application.port.out.persistence.BookPersistenceOutPort;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class PublishNewRatingService {

    private final BookPersistenceOutPort bookRepository;
    private final BookRatingMessagingOutPort bookRatingMessagingOutPort;

    @SneakyThrows
    public void publishRating(Long bookId) {
        var book = bookRepository.findById(bookId).orElseThrow();
        bookRatingMessagingOutPort.sendMessage(book);
    }

}
