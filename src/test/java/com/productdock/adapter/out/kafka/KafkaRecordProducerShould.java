package com.productdock.adapter.out.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class KafkaRecordProducerShould {

    private static final BookRatingMessage BOOK_RATING_MESSAGE = BookRatingMessage.builder().bookId(1L).rating(5.0).ratingsCount(1).build();
    private static final String BOOK_RATING_TOPIC = "book-rating";

    @InjectMocks
    private KafkaRecordProducer kafkaRecordProducer;

    @Test
    void produceMessage() throws JsonProcessingException {
        var producerRecord = kafkaRecordProducer.createKafkaRecord(BOOK_RATING_TOPIC, BOOK_RATING_MESSAGE);

        String desiredValue = "{\"bookId\":" + BOOK_RATING_MESSAGE.getBookId() +
                ",\"rating\":" + BOOK_RATING_MESSAGE.getRating() + ",\"ratingsCount\":" + BOOK_RATING_MESSAGE.getRatingsCount() + "}";

        assertThat(producerRecord.value()).isEqualTo(desiredValue);
    }
}
