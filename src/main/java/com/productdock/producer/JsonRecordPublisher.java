package com.productdock.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class JsonRecordPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final JsonRecordProducer recordProducer;

    public void sendMessage(String kafkaTopic, Object message) throws ExecutionException, InterruptedException, JsonProcessingException {
        var kafkaRecord = recordProducer.createKafkaRecord(kafkaTopic, message);
        kafkaTemplate.send(kafkaRecord).get();
    }
}
