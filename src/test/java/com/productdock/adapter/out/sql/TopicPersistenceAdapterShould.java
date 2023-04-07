package com.productdock.adapter.out.sql;

import com.productdock.adapter.out.sql.entity.TopicJpaEntity;
import com.productdock.adapter.out.sql.mapper.TopicMapper;
import com.productdock.domain.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class TopicPersistenceAdapterShould {

    @InjectMocks
    private TopicPersistenceAdapter topicPersistenceAdapter;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private TopicMapper topicMapper;

    private static final TopicJpaEntity FIRST_TOPIC_ENTITY = mock(TopicJpaEntity.class);
    private static final TopicJpaEntity SECOND_TOPIC_ENTITY = mock(TopicJpaEntity.class);

    private static final Book.Topic FIRST_TOPIC = mock(Book.Topic.class);
    private static final Book.Topic SECOND_TOPIC = mock(Book.Topic.class);

    private static final List TOPIC_IDS = List.of(1L, 2L);

    @Test
    void findTopicsByIdsWhenIdsExist() {
        var topicEntities = List.of(FIRST_TOPIC_ENTITY, SECOND_TOPIC_ENTITY);
        var topics = List.of(FIRST_TOPIC, SECOND_TOPIC);

        given(topicRepository.findByIds(TOPIC_IDS)).willReturn(topicEntities);
        given(topicMapper.toDomainCollection(topicEntities)).willReturn(topics);

        var foundedTopics = topicPersistenceAdapter.findByIds(TOPIC_IDS);

        assertThat(foundedTopics).contains(FIRST_TOPIC, SECOND_TOPIC);
    }

    @Test
    void findTopicsByIdsWhenIdsNotExist() {
        given(topicRepository.findByIds(TOPIC_IDS)).willReturn(new ArrayList());

        var foundedTopics = topicPersistenceAdapter.findByIds(TOPIC_IDS);

        assertThat(foundedTopics).isEmpty();
    }

    @Test
    void findAllTopics() {
        var topicEntities = List.of(FIRST_TOPIC_ENTITY, SECOND_TOPIC_ENTITY);
        var topics = List.of(FIRST_TOPIC, SECOND_TOPIC);

        given(topicRepository.findAll()).willReturn(topicEntities);
        given(topicMapper.toDomainCollection(topicEntities)).willReturn(topics);

        var foundedTopics = topicPersistenceAdapter.findAll();

        assertThat(foundedTopics).contains(FIRST_TOPIC, SECOND_TOPIC);
    }
}
