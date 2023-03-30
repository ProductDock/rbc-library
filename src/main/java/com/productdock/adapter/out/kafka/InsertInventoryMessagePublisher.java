package com.productdock.adapter.out.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.productdock.adapter.out.kafka.messages.InsertInventoryMessage;
import com.productdock.adapter.out.kafka.publisher.KafkaPublisher;
import com.productdock.application.port.out.messaging.BookInventoryMessagingOutPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@RequiredArgsConstructor
class InsertInventoryMessagePublisher implements BookInventoryMessagingOutPort {

    @Value("${spring.kafka.topic.insert-inventory}")
    private String kafkaTopic;
    private final KafkaPublisher publisher;

    @Override
    public void sendMessage(Long bookId, int bookCopies) throws ExecutionException, InterruptedException, JsonProcessingException {
        var message = InsertInventoryMessage.builder()
                .bookId(bookId)
                .bookCopies(bookCopies).build();
        publisher.sendMessage(message, kafkaTopic);
    }
}
