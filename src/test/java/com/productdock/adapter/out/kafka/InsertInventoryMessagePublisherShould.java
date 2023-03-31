package com.productdock.adapter.out.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.productdock.adapter.out.kafka.messages.InsertInventoryMessage;
import com.productdock.adapter.out.kafka.publisher.KafkaPublisher;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class InsertInventoryMessagePublisherShould {

    @InjectMocks
    private InsertInventoryMessagePublisher insertInventoryMessagePublisher;

    @Mock
    private KafkaPublisher publisher;

    @Captor
    private ArgumentCaptor<InsertInventoryMessage> insertInventoryMessageCaptor;

    private static final Long DEFAULT_BOOK_ID = 33L;
    private static final int DEFAULT_BOOK_COPIES = 2;

    @Test
    void sendMessage() throws ExecutionException, InterruptedException, JsonProcessingException {
        insertInventoryMessagePublisher.sendMessage(DEFAULT_BOOK_ID, DEFAULT_BOOK_COPIES);

        verify(publisher).sendMessage(insertInventoryMessageCaptor.capture(), any());
        var capturedMessage = insertInventoryMessageCaptor.getValue();

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(capturedMessage.getBookId()).isEqualTo(DEFAULT_BOOK_ID);
            softly.assertThat(capturedMessage.getBookCopies()).isEqualTo(DEFAULT_BOOK_COPIES);
        }
    }
}
