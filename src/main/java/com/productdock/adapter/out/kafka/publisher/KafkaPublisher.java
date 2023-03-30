package com.productdock.adapter.out.kafka.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class KafkaPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaRecordProducer kafkaRecordProducer;

    public KafkaPublisher(KafkaTemplate<String, String> kafkaTemplate, KafkaRecordProducer kafkaRecordProducer) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaRecordProducer = kafkaRecordProducer;
    }

    public void sendMessage(Object message, String kafkaTopic) throws ExecutionException, InterruptedException, JsonProcessingException {
        log.debug("Sent kafka message: {} on kafka topic: {}", message, kafkaTopic);
        var kafkaRecord = kafkaRecordProducer.createKafkaRecord(kafkaTopic, message);
        kafkaTemplate.send(kafkaRecord).get();
    }
}
