package com.productdock.data.provider.domain;

import com.productdock.domain.Book;

public class TopicMother {
    private static final Long defaultId = null;
    private static final String defaultName = "::name::";

    public static Book.Topic.TopicBuilder defaultTopicBuilder() {
        return Book.Topic.builder()
                .id(defaultId)
                .name(defaultName);
    }

    public static Book.Topic defaultTopic() {
        return defaultTopicBuilder().build();
    }
}
