package com.productdock.application.service;

import com.productdock.application.port.out.persistence.TopicPersistenceOutPort;
import com.productdock.domain.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class GetTopicServiceShould {

    @InjectMocks
    private GetTopicService getTopicService;

    @Mock
    private TopicPersistenceOutPort topicPersistenceOutPort;

    private static final Collection<Book.Topic> TOPICS = List.of(mock(Book.Topic.class), mock(Book.Topic.class));

    @Test
    void getAllTopics() {
        given(topicPersistenceOutPort.findAll()).willReturn(TOPICS);

        var topics = getTopicService.getAll();

        assertThat(topics).containsExactlyElementsOf(TOPICS);
    }
}
