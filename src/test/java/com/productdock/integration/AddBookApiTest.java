package com.productdock.integration;

import com.productdock.adapter.out.kafka.messages.AddedBookMessage;
import com.productdock.adapter.out.sql.TopicRepository;
import com.productdock.adapter.out.sql.entity.TopicJpaEntity;
import com.productdock.data.provider.out.kafka.KafkaTestBase;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

import static com.productdock.kafka.KafkaFileUtil.getMessageFrom;
import static com.productdock.kafka.KafkaFileUtil.ifFileExists;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles({"in-memory-db"})
class AddBookApiTest extends KafkaTestBase {

    public static final String TEST_FILE = "testAddBook.txt";

    private static final String DEFAULT_TOPIC = "default topic";
    private static final String ROLE_ADMIN = "SCOPE_ROLE_ADMIN";
    private static final String ROLE_USER = "SCOPE_ROLE_USER";

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private RestRequestProducer requestProducer;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    final void before() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "book_topic", "book", "topic");
    }

    @AfterAll
    static void after() {
        File file = new File(TEST_FILE);
        file.delete();
    }

    @Test
    @WithMockUser
    void addBook_whenBookIsValid() throws Exception {
        var firstTopic = givenTopicWithName(DEFAULT_TOPIC);
        var insertBookDtoJson = "{\"title\": \"::title::\", " +
                "\"author\": \"::author::\", " +
                "\"cover\": \"::cover::\", " +
                "\"description\": \"::description::\", " +
                "\"topics\": [{\"id\": " + firstTopic.getId() + " }], " +
                "\"bookCopies\" : 2 }";

        var resultActions = requestProducer.makeAddBookRequestAs(insertBookDtoJson, ROLE_ADMIN).andExpect(status().isCreated());
        var result = resultActions.andReturn();
        String insertedBookId = result.getResponse().getContentAsString();

        requestProducer.makeGetBookRequest(Long.valueOf(insertedBookId))
                .andExpect(content().json(
                        "{\"id\":" + insertedBookId + "," +
                                "\"title\":\"::title::\"," +
                                "\"author\":\"::author::\"," +
                                "\"cover\": \"::cover::\"," +
                                "\"topics\": " + JsonFrom.topicCollection(List.of(firstTopic)) + "," +
                                "\"reviews\": [] }"));

        await()
                .atMost(Duration.ofSeconds(4))
                .until(ifFileExists(TEST_FILE));

        var insertBookMessage = (AddedBookMessage) getMessageFrom(TEST_FILE);
        assertThatInsertBookMessageMatching(insertBookMessage, insertedBookId, firstTopic.getId());
    }

    @Test
    @WithMockUser
    void returnForbidden_whenUserWithInsufficientRole() throws Exception {
        var anyJson = "{}";

        requestProducer.makeAddBookRequestAs(anyJson, ROLE_USER)
                .andExpect(status().isForbidden());
    }

    @WithMockUser
    @ParameterizedTest
    @MethodSource("invalidBooksArguments")
    void returnBadRequest_whenBookIsInvalid(String insertBookDtoJson) throws Exception {

        requestProducer.makeAddBookRequestAs(insertBookDtoJson, ROLE_ADMIN)
                .andExpect(status().isBadRequest());
    }

    static Stream<Arguments> invalidBooksArguments() {
        return Stream.of(
                Arguments.of("{}"),
                Arguments.of("{\"title\": \"::title::\", " +
                        "\"author\": \"::author::\", " +
                        "\"cover\": \"::cover::\", " +
                        "\"description\": \"::description::\", " +
                        "\"topics\": [{\"id\": 1 }], " +
                        "\"bookCopies\" : 2 }"),
                Arguments.of("{\"title\": \"::title::\", " +
                        "\"author\": \"::author::\", " +
                        "\"cover\": \"::cover::\", " +
                        "\"description\": \"::description::\", " +
                        "\"topics\": [], " +
                        "\"bookCopies\" : 0 }")
        );
    }

    private void assertThatInsertBookMessageMatching(AddedBookMessage addedBookMessage, String bookId, Long topicId) {
        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(Long.toString(addedBookMessage.getBookId())).isEqualTo(bookId);
            softly.assertThat(addedBookMessage.getTitle()).isEqualTo("::title::");
            softly.assertThat(addedBookMessage.getAuthor()).isEqualTo("::author::");
            softly.assertThat(addedBookMessage.getCover()).isEqualTo("::cover::");
            softly.assertThat(addedBookMessage.getBookCopies()).isEqualTo(2);
            softly.assertThat(addedBookMessage.getTopics())
                    .extracting("id", "name")
                    .containsExactly(
                            tuple(Long.toString(topicId), DEFAULT_TOPIC));
        }
    }

    private TopicJpaEntity givenTopicWithName(String name) {
        var topic = TopicJpaEntity.builder().name(name).build();
        return topicRepository.save(topic);
    }


}
