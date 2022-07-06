package com.productdock.data.provider.provider;

import com.productdock.adapter.out.postresql.entity.TopicEntity;

public class TopicEntityMother {

    private static final Long defaultId = null;
    private static final String defaultName = "::topicName::";

    public static TopicEntity.TopicEntityBuilder defaultTopicBuilder() {
        return TopicEntity.builder()
                .id(defaultId)
                .name(defaultName);
    }

    public static TopicEntity defaultTopic() {
        return defaultTopicBuilder().build();
    }
}
