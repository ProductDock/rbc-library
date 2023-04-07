package com.productdock.adapter.in.web.mapper;

import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static com.productdock.data.provider.domain.TopicMother.defaultTopicBuilder;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

class TopicDtoMapperShould {

    private TopicDtoMapper topicDtoMapper = Mappers.getMapper(TopicDtoMapper.class);

    @Test
    void mapTopicCollectionToTopicDtoCollection() {
        var managementTopic = defaultTopicBuilder().id(1L).name("Management").build();
        var designTopic = defaultTopicBuilder().id(2L).name("Design").build();

        var topicDtoCollection = topicDtoMapper.toDtoCollection(List.of(managementTopic, designTopic));

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(topicDtoCollection)
                    .extracting("id", "name")
                    .containsExactlyInAnyOrder(
                            tuple(managementTopic.getId(), managementTopic.getName()),
                            tuple(designTopic.getId(), designTopic.getName()));

        }
    }
}
