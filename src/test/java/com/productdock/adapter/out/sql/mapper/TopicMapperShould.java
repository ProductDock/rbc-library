package com.productdock.adapter.out.sql.mapper;

import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static com.productdock.data.provider.out.sql.TopicJpaEntityMother.defaultTopicJpaEntityBuilder;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

class TopicMapperShould {

    private TopicMapper topicMapper = Mappers.getMapper(TopicMapper.class);

    private static final Long FIRST_TOPIC_ID = 1L;
    private static final Long SECOND_TOPIC_ID = 2L;

    @Test
    void mapTopicJpaEntityCollectionToTopicCollection() {
        var firstTopic = defaultTopicJpaEntityBuilder().id(FIRST_TOPIC_ID).build();
        var secondTopic = defaultTopicJpaEntityBuilder().id(SECOND_TOPIC_ID).name("::secondTopic::").build();

        var topics = topicMapper.toDomainCollection(List.of(firstTopic, secondTopic));

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(topics)
                    .extracting("id", "name")
                    .containsExactlyInAnyOrder(
                            tuple(firstTopic.getId(), firstTopic.getName()),
                            tuple(secondTopic.getId(), secondTopic.getName()));
        }
    }

}
