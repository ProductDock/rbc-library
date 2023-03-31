package com.productdock.adapter.out.kafka;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.productdock.adapter.out.kafka.mapper.InsertBookMessageMapper;
import com.productdock.adapter.out.kafka.messages.InsertBookMessage;
import com.productdock.adapter.out.kafka.publisher.KafkaPublisher;
import com.productdock.domain.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InsertBookMessagePublisherShould {

    @InjectMocks
    private InsertBookMessagePublisher insertBookMessagePublisher;

    @Mock
    private KafkaPublisher publisher;

    @Mock
    private InsertBookMessageMapper insertBookMessageMapper;

    @Test
    void sendMessage() throws ExecutionException, InterruptedException, JsonProcessingException {
        var book = mock(Book.class);
        var bookCopies = 2;
        var insertBookMessage = mock(InsertBookMessage.class);
        when(insertBookMessageMapper.toMessage(book, bookCopies)).thenReturn(insertBookMessage);

        insertBookMessagePublisher.sendMessage(book, bookCopies);

        verify(publisher).sendMessage(insertBookMessage, null);
    }

}
