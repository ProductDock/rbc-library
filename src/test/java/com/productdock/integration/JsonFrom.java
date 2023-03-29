package com.productdock.integration;

import com.productdock.adapter.out.sql.entity.TopicJpaEntity;

import java.util.Collection;

public class JsonFrom {

    public static String topicCollection(Collection<TopicJpaEntity> topics) {
        var topicsJson = new StringBuilder("[");
        for (var topic: topics) {
            topicsJson.append("{ \"id\": ").append(topic.getId()).append(", \"name\":\"").append(topic.getName()).append("\" },");
        }
        topicsJson.setLength(topicsJson.length() - 1);
        return topicsJson.append("]").toString();
    }
}
