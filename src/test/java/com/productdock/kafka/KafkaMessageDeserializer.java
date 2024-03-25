package com.productdock.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.productdock.adapter.out.kafka.messages.AddedBookMessage;
import com.productdock.adapter.out.kafka.messages.BookRatingMessage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

@Component
public record KafkaMessageDeserializer(ObjectMapper objectMapper) {

    public BookRatingMessage deserializeBookRatingMessage(ConsumerRecord<String, String> consumerRating) throws JsonProcessingException {
        return objectMapper.readValue(consumerRating.value(), BookRatingMessage.class);
    }

    public AddedBookMessage deserializeInsertBookMessage(ConsumerRecord<String, String> consumerInsertBook) throws JsonProcessingException {
        return objectMapper.readValue(consumerInsertBook.value(), AddedBookMessage.class);
    }

    public Long deserializeDeleteBookMessage(ConsumerRecord<String, String> consumerRecord) throws JsonProcessingException {
        return objectMapper.readValue(consumerRecord.value(),Long.class);
    }
}
