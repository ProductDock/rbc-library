package com.productdock.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.productdock.book.BookRatingMessage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

@Component
public record BookRatingMessageDeserializer(ObjectMapper objectMapper) {

    public BookRatingMessage deserializeBookRatingMessage(ConsumerRecord<String, String> consumerRating) throws JsonProcessingException {
        return objectMapper.readValue(consumerRating.value(), BookRatingMessage.class);
    }
}
