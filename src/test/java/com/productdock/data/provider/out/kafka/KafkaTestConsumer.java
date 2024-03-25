package com.productdock.data.provider.out.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.productdock.kafka.KafkaMessageDeserializer;
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
    private KafkaMessageDeserializer kafkaMessageDeserializer;


    @KafkaListener(topics = "${spring.kafka.topic.book-rating}")
    public void receiveBookRating(ConsumerRecord<String, String> consumerRating) throws JsonProcessingException {
        LOGGER.info("received payload='{}'", consumerRating.toString());
        var bookRatingMessage = kafkaMessageDeserializer.deserializeBookRatingMessage(consumerRating);
        writeRecordToFile(bookRatingMessage, "testRating.txt");
    }

    @KafkaListener(topics = "${spring.kafka.topic.insert-book}")
    public void receiveInsertBook(ConsumerRecord<String, String> consumerInsertBook) throws JsonProcessingException {
        LOGGER.info("received payload='{}'", consumerInsertBook.toString());
        var insertBookMessage = kafkaMessageDeserializer.deserializeInsertBookMessage(consumerInsertBook);
        writeRecordToFile(insertBookMessage, "testAddBook.txt");
    }

    @KafkaListener(topics = "${spring.kafka.topic.delete-book}")
    public void recieveDeleteBook(ConsumerRecord<String, String> consumerRecord) throws JsonProcessingException{
        var deleteBookMessage = kafkaMessageDeserializer.deserializeDeleteBookMessage(consumerRecord);
        writeRecordToFile(deleteBookMessage, "testDeleteBook.txt");
    }

    private void writeRecordToFile(Object message, String fileName) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(message);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
