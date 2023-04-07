package com.productdock.integration;

import com.productdock.adapter.out.sql.TopicRepository;
import com.productdock.adapter.out.sql.entity.TopicJpaEntity;
import com.productdock.data.provider.out.kafka.KafkaTestBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.jdbc.JdbcTestUtils;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles({"in-memory-db"})
class GetTopicApiTest extends KafkaTestBase {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private RestRequestProducer requestProducer;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    final void before() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "book_topic", "review", "review", "book", "topic");
    }

    @Test
    @WithMockUser
    void getTopics() throws Exception {
        var marketingTopic = givenTopicWithName("Marketing");
        var softwareArchitectureTopic = givenTopicWithName("Software architecture");

        requestProducer.makeGetTopicsRequest()
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "[{\"id\":" + marketingTopic.getId() + "," +
                                "\"name\":\"" + marketingTopic.getName() + "\"}," +
                                "{\"id\":" + softwareArchitectureTopic.getId() + "," +
                                "\"name\":\"" + softwareArchitectureTopic.getName() + "\"}]"));
    }

    private TopicJpaEntity givenTopicWithName(String name) {
        var topic = TopicJpaEntity.builder().name(name).build();
        return topicRepository.save(topic);
    }
}
