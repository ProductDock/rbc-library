package com.productdock.integration;

import com.productdock.adapter.out.kafka.messages.InsertBookMessage;
import com.productdock.adapter.out.kafka.messages.InsertInventoryMessage;
import com.productdock.adapter.out.sql.TopicRepository;
import com.productdock.adapter.out.sql.entity.TopicJpaEntity;
import com.productdock.data.provider.out.kafka.KafkaTestBase;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles({"in-memory-db"})
class CreateBookApiTest extends KafkaTestBase {

    public static final String INSERT_BOOK_TEST_FILE = "testInsertBook.txt";
    public static final String INSERT_INVENTORY_TEST_FILE = "testInsertInventory.txt";

    private static final String DEFAULT_TOPIC = "default topic";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_USER = "ROLE_USER";

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
        File firstFile = new File(INSERT_INVENTORY_TEST_FILE);
        File secondFile = new File(INSERT_BOOK_TEST_FILE);
        firstFile.delete();
        secondFile.delete();
    }

    @Test
    @WithMockUser
    void createBook_whenBookIsValid() throws Exception {
        var firstTopic = givenTopicWithName(DEFAULT_TOPIC);
        var insertBookDtoJson = "{\"title\": \"::title::\", " +
                "\"author\": \"::author::\", " +
                "\"cover\": \"::cover::\", " +
                "\"description\": \"::description::\", " +
                "\"topics\": [{\"id\": " + firstTopic.getId() + " }], " +
                "\"bookCopies\" : 2 }";

        var resultActions = requestProducer.makeCreateBookRequestAs(insertBookDtoJson, ROLE_ADMIN).andExpect(status().isCreated());
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
                .until(ifFileExists(INSERT_INVENTORY_TEST_FILE));

        var insertBookMessage = (InsertBookMessage) getMessageFrom(INSERT_BOOK_TEST_FILE);
        var insertInventoryMessage = (InsertInventoryMessage) getMessageFrom(INSERT_INVENTORY_TEST_FILE);

        assertThatInsertBookMessageMatching(insertBookMessage, insertedBookId, firstTopic.getId());
        assertThatInsertInventoryMessageMatching(insertInventoryMessage, insertedBookId);

    }

    @Test
    @WithMockUser
    void returnForbidden_whenUserWithInsufficientRole() throws Exception {
        var anyJson = "{}";

        requestProducer.makeCreateBookRequestAs(anyJson, ROLE_USER)
                .andExpect(status().isForbidden());
    }

    @WithMockUser
    @ParameterizedTest
    @MethodSource("invalidBooksArguments")
    void returnBadRequest_whenBookIsInvalid(String insertBookDtoJson) throws Exception {

        requestProducer.makeCreateBookRequestAs(insertBookDtoJson, ROLE_ADMIN)
                .andExpect(status().isBadRequest());
    }

    static Stream<Arguments> invalidBooksArguments() {
        return Stream.of(
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

    private void assertThatInsertBookMessageMatching(InsertBookMessage insertBookMessage, String bookId, Long topicId) {
        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(Long.toString(insertBookMessage.getBookId())).isEqualTo(bookId);
            softly.assertThat(insertBookMessage.getTitle()).isEqualTo("::title::");
            softly.assertThat(insertBookMessage.getAuthor()).isEqualTo("::author::");
            softly.assertThat(insertBookMessage.getCover()).isEqualTo("::cover::");
            softly.assertThat(insertBookMessage.getBookCopies()).isEqualTo(2);
            softly.assertThat(insertBookMessage.getTopics())
                    .extracting("id", "name")
                    .containsExactly(
                            tuple(Long.toString(topicId), DEFAULT_TOPIC));
        }
    }

    private void assertThatInsertInventoryMessageMatching(InsertInventoryMessage insertInventoryMessage, String bookId) {
        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(Long.toString(insertInventoryMessage.getBookId())).isEqualTo(bookId);
            softly.assertThat(insertInventoryMessage.getBookCopies()).isEqualTo(2);
        }
    }

    private Callable<Boolean> ifFileExists(String testFile) {
        return () -> {
            File f = new File(testFile);
            return f.isFile();
        };
    }

    private Object getMessageFrom(String testFile) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(testFile);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        var bookRatingMessage = objectInputStream.readObject();
        objectInputStream.close();
        return bookRatingMessage;
    }

    private TopicJpaEntity givenTopicWithName(String name) {
        var topic = TopicJpaEntity.builder().name(name).build();
        return topicRepository.save(topic);
    }


}
