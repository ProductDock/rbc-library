package com.productdock.adapter.out.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.productdock.application.port.out.messaging.BookMessagingOutPort;
import com.productdock.domain.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@RequiredArgsConstructor
class BookRatingMessagePublisher implements BookMessagingOutPort {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaRecordProducer recordProducer;

    @Override
    public void sendRatingMessage(String kafkaTopic, Book book) throws ExecutionException, InterruptedException, JsonProcessingException {
        var message = BookRatingMessage.builder()
                .bookId(book.getId())
                .rating(book.getRating().getScore())
                .ratingsCount(book.getRating().getCount())
                .build();
        var kafkaRecord = recordProducer.createKafkaRecord(kafkaTopic, message);
        log.debug("Publishing Kafka message [{}]", kafkaRecord);
        kafkaTemplate.send(kafkaRecord).get();
    }
}
