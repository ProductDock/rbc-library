package com.productdock.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.productdock.adapter.out.kafka.messages.BookRatingMessage;
import com.productdock.adapter.out.kafka.messages.InsertBookMessage;
import com.productdock.adapter.out.kafka.messages.InsertInventoryMessage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

@Component
public record KafkaMessageDeserializer(ObjectMapper objectMapper) {

    public BookRatingMessage deserializeBookRatingMessage(ConsumerRecord<String, String> consumerRating) throws JsonProcessingException {
        return objectMapper.readValue(consumerRating.value(), BookRatingMessage.class);
    }

    public InsertBookMessage deserializeInsertBookMessage(ConsumerRecord<String, String> consumerInsertBook) throws JsonProcessingException {
        return objectMapper.readValue(consumerInsertBook.value(), InsertBookMessage.class);
    }

    public InsertInventoryMessage deserializeInsertInventoryMessage(ConsumerRecord<String, String> consumerInsertInventory) throws JsonProcessingException {
        return objectMapper.readValue(consumerInsertInventory.value(), InsertInventoryMessage.class);
    }
}
