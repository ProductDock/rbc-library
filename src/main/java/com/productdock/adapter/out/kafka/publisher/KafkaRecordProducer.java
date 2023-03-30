package com.productdock.adapter.out.kafka.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class KafkaRecordProducer {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public ProducerRecord<String, String> createKafkaRecord(String topic, Object message) throws JsonProcessingException {
        log.debug("Create kafka record on topic {} with message: {}", topic, message);
        var serialisedMessage = serialiseMessage(message);
        return new ProducerRecord<>(topic, UUID.randomUUID().toString(), serialisedMessage);
    }

    private String serialiseMessage(Object message) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(message);
    }
}
