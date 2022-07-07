package com.productdock.data.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.productdock.adapter.out.kafka.BookRatingMessage;
import com.productdock.kafka.BookRatingMessageDeserializer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;


@Component
public class KafkaTestConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaTestConsumer.class);

    @Autowired
    private BookRatingMessageDeserializer bookRatingMessageDeserializer;


    @KafkaListener(topics = "${spring.kafka.topic.book-rating}")
    public void receive(ConsumerRecord<String, String> consumerRating) throws JsonProcessingException {
        LOGGER.info("received payload='{}'", consumerRating.toString());
        var bookRatingMessage = bookRatingMessageDeserializer.deserializeBookRatingMessage(consumerRating);
        writeRecordToFile(bookRatingMessage);
    }

    private void writeRecordToFile(BookRatingMessage bookRatingMessage) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("testRating.txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(bookRatingMessage);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
