package com.productdock.adapter.out.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.productdock.adapter.out.kafka.messages.BookRatingMessage;
import com.productdock.adapter.out.kafka.publisher.KafkaPublisher;
import com.productdock.domain.Book;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookRatingMessagePublisherShould {

    @InjectMocks
    private BookRatingMessagePublisher bookRatingMessagePublisher;

    @Mock
    private KafkaPublisher publisher;

    @Captor
    private ArgumentCaptor<BookRatingMessage> bookRatingMessageCaptor;

    @Test
    void sendMessage() throws ExecutionException, InterruptedException, JsonProcessingException {
        var book = mock(Book.class);
        var rating = mock(Book.Rating.class);
        when(book.getRating()).thenReturn(rating);

        bookRatingMessagePublisher.sendMessage(book);

        verify(publisher).sendMessage(bookRatingMessageCaptor.capture(), any());
        var capturedMessage = bookRatingMessageCaptor.getValue();

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(capturedMessage.getBookId()).isEqualTo(book.getId());
            softly.assertThat(capturedMessage.getRating()).isEqualTo(rating.getScore());
            softly.assertThat(capturedMessage.getRatingsCount()).isEqualTo(rating.getCount());
        }
    }

}
