package com.productdock.adapter.out.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.productdock.adapter.out.kafka.mapper.AddedBookMessageMapper;
import com.productdock.adapter.out.kafka.publisher.KafkaPublisher;
import com.productdock.application.port.out.messaging.BookCatalogMessagingOutPort;
import com.productdock.application.port.out.messaging.DeleteBookMessagingOutPort;
import com.productdock.domain.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class DeletedBookMessagePublisher implements DeleteBookMessagingOutPort {

    @Value("${spring.kafka.topic.delete-book}")
    private String kafkaTopic;
    private final KafkaPublisher publisher;

    @Override
    public void sendMessage(Long bookId) throws ExecutionException, InterruptedException, JsonProcessingException {
        publisher.sendMessage(bookId, kafkaTopic);
    }

}
