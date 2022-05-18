package com.productdock.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.productdock.book.BookRatingMessage;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RecordProducer {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public ProducerRecord<String, String> createKafkaRecord(String topic, BookRatingMessage bookRatingMessage) throws JsonProcessingException {
        var serialisedMessage = serialiseMessage(bookRatingMessage);
        return new ProducerRecord<>(topic, UUID.randomUUID().toString(), serialisedMessage);
    }

    private String serialiseMessage(BookRatingMessage bookRatingMessage) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(bookRatingMessage);
    }
}
