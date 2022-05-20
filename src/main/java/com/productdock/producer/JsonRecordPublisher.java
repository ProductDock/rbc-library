package com.productdock.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class JsonRecordPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final JsonRecordProducer recordProducer;

    @Value("${spring.kafka.topic.book-rating}")
    private String kafkaTopic;

    public void sendMessage(Object object) throws ExecutionException, InterruptedException, JsonProcessingException {
        var kafkaRecord = recordProducer.createKafkaRecord(kafkaTopic, object);
        kafkaTemplate.send(kafkaRecord).get();
    }
}
