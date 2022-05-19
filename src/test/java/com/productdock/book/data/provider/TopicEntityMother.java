package com.productdock.book.data.provider;

import com.productdock.book.TopicEntity;

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
