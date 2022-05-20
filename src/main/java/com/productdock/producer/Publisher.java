package com.productdock.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.productdock.book.BookRatingMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class Publisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final JsonRecordProducer recordProducer;

    @Value("${spring.kafka.topic.book-rating}")
    private String kafkaTopic;

    public Publisher(KafkaTemplate<String, String> kafkaTemplate, JsonRecordProducer recordProducer) {
        this.kafkaTemplate = kafkaTemplate;
        this.recordProducer = recordProducer;
    }

    public void sendMessage(BookRatingMessage bookRatingMessage) throws ExecutionException, InterruptedException, JsonProcessingException {
        var kafkaRecord = recordProducer.createKafkaRecord(kafkaTopic, bookRatingMessage);
        kafkaTemplate.send(kafkaRecord).get();
    }
}
