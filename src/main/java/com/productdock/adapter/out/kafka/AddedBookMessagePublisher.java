package com.productdock.adapter.out.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.productdock.adapter.out.kafka.mapper.AddedBookMessageMapper;
import com.productdock.adapter.out.kafka.publisher.KafkaPublisher;
import com.productdock.application.port.out.messaging.BookCatalogMessagingOutPort;
import com.productdock.domain.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@RequiredArgsConstructor
class AddedBookMessagePublisher implements BookCatalogMessagingOutPort {

    @Value("${spring.kafka.topic.insert-book}")
    private String kafkaTopic;
    private final AddedBookMessageMapper addedBookMessageMapper;
    private final KafkaPublisher publisher;

    @Override
    public void sendMessage(Book book, int bookCopies) throws ExecutionException, InterruptedException, JsonProcessingException {
        var message = addedBookMessageMapper.toMessage(book, bookCopies);
        publisher.sendMessage(message, kafkaTopic);
    }
}
