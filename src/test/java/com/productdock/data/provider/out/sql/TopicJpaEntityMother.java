package com.productdock.data.provider.out.sql;

import com.productdock.adapter.out.sql.entity.TopicJpaEntity;

public class TopicJpaEntityMother {
    private static final Long defaultId = null;
    private static final String defaultName = "::title::";

    public static TopicJpaEntity.TopicJpaEntityBuilder defaultTopicJpaEntityBuilder() {
        return TopicJpaEntity.builder()
                .id(defaultId)
                .name(defaultName);
    }

    public static TopicJpaEntity defaultTopicJpaEntity() {
        return TopicJpaEntity.builder()
                .id(defaultId)
                .name(defaultName).build();
    }

}
