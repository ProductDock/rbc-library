package com.productdock.application.service;

import com.productdock.application.port.out.messaging.BookMessagingOutPort;
import com.productdock.application.port.out.persistence.BookPersistenceOutPort;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class PublishNewRatingService {

    private final BookPersistenceOutPort bookRepository;
    private final BookMessagingOutPort bookMessagingOutPort;

    @Value("${spring.kafka.topic.book-rating}")
    private String kafkaTopic;

    @SneakyThrows
    public void publishRating(Long bookId) {
        var book = bookRepository.findById(bookId).orElseThrow();
        bookMessagingOutPort.sendRatingMessage(kafkaTopic, book);
    }

}
