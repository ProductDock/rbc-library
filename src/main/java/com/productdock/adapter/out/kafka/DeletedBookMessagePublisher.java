package com.productdock.adapter.out.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.productdock.adapter.out.kafka.publisher.KafkaPublisher;
import com.productdock.application.port.out.messaging.DeleteBookMessagingOutPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
class DeletedBookMessagePublisher implements DeleteBookMessagingOutPort {

    @Value("${spring.kafka.topic.delete-book}")
    private String kafkaTopic;
    private final KafkaPublisher publisher;

    @Override
    public void sendMessage(Long bookId) throws ExecutionException, InterruptedException, JsonProcessingException {
        publisher.sendMessage(bookId, kafkaTopic);
    }

}
