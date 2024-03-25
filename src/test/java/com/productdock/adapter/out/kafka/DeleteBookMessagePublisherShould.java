package com.productdock.adapter.out.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.productdock.adapter.out.kafka.publisher.KafkaPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DeleteBookMessagePublisherShould {
    public static final long BOOK_ID = 1;

    @InjectMocks
    private DeletedBookMessagePublisher deletedBookMessagePublisher;
    @Mock
    private KafkaPublisher publisher;

    @Test
    void sendMessage() throws ExecutionException, InterruptedException, JsonProcessingException {

        deletedBookMessagePublisher.sendMessage(BOOK_ID);

        verify(publisher).sendMessage(BOOK_ID, null);
    }
}
