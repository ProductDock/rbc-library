package com.productdock.adapter.in.web;

import com.productdock.adapter.in.web.dto.GetBookDto;
import com.productdock.adapter.in.web.mapper.TopicDtoMapper;
import com.productdock.application.port.in.GetTopicQuery;
import com.productdock.domain.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class GetTopicApiShould {

    @InjectMocks
    private GetTopicApi getTopicApi;

    @Mock
    private GetTopicQuery getTopicQuery;

    @Mock
    private TopicDtoMapper topicDtoMapper;


    @Test
    void getTopics() {
        var firstTopic = mock(Book.Topic.class);
        var secondTopic = mock(Book.Topic.class);
        var firstTopicDto = mock(GetBookDto.TopicDto.class);
        var secondTopicDto = mock(GetBookDto.TopicDto.class);
        var topics = List.of(firstTopic, secondTopic);

        given(getTopicQuery.getAll()).willReturn(topics);
        given(topicDtoMapper.toDtoCollection(topics)).willReturn(List.of(firstTopicDto, secondTopicDto));

        var topicDtoCollection = getTopicApi.getTopics();

        assertThat(topicDtoCollection).contains(firstTopicDto, secondTopicDto);
    }
}
