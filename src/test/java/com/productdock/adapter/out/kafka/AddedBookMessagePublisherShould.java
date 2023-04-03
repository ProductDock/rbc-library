package com.productdock.adapter.out.kafka;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.productdock.adapter.out.kafka.mapper.AddedBookMessageMapper;
import com.productdock.adapter.out.kafka.messages.AddedBookMessage;
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
class AddedBookMessagePublisherShould {

    @InjectMocks
    private AddedBookMessagePublisher addedBookMessagePublisher;

    @Mock
    private KafkaPublisher publisher;

    @Mock
    private AddedBookMessageMapper addedBookMessageMapper;

    @Test
    void sendMessage() throws ExecutionException, InterruptedException, JsonProcessingException {
        var book = mock(Book.class);
        var bookCopies = 2;
        var insertBookMessage = mock(AddedBookMessage.class);
        when(addedBookMessageMapper.toMessage(book, bookCopies)).thenReturn(insertBookMessage);

        addedBookMessagePublisher.sendMessage(book, bookCopies);

        verify(publisher).sendMessage(insertBookMessage, null);
    }

}
