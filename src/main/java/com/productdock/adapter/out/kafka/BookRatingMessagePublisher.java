package com.productdock.adapter.out.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.productdock.adapter.out.kafka.messages.BookRatingMessage;
import com.productdock.adapter.out.kafka.publisher.KafkaPublisher;
import com.productdock.application.port.out.messaging.BookRatingMessagingOutPort;
import com.productdock.domain.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@RequiredArgsConstructor
class BookRatingMessagePublisher implements BookRatingMessagingOutPort {

    @Value("${spring.kafka.topic.book-rating}")
    private String kafkaTopic;
    private final KafkaPublisher publisher;

    @Override
    public void sendMessage(Book book) throws ExecutionException, InterruptedException, JsonProcessingException {
        var message = BookRatingMessage.builder()
                .bookId(book.getId())
                .rating(book.getRating().getScore())
                .ratingsCount(book.getRating().getCount())
                .build();
        publisher.sendMessage(message, kafkaTopic);
    }
}
