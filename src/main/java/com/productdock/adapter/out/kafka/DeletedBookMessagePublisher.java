package com.productdock.adapter.out.kafka;

import com.productdock.adapter.out.kafka.publisher.KafkaPublisher;
import com.productdock.application.port.out.messaging.DeleteBookMessagingOutPort;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeletedBookMessagePublisher implements DeleteBookMessagingOutPort {

    @Value("${spring.kafka.topic.delete-book}")
    private String kafkaTopic;
    private final KafkaPublisher publisher;

    @SneakyThrows
    @Override
    public void sendMessage(Long bookId) {
        publisher.sendMessage(bookId, kafkaTopic);
    }

}
