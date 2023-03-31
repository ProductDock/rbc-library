package com.productdock.integration;

import com.productdock.adapter.out.kafka.messages.BookRatingMessage;
import com.productdock.adapter.out.sql.BookRepository;
import com.productdock.adapter.out.sql.ReviewRepository;
import com.productdock.adapter.out.sql.TopicRepository;
import com.productdock.adapter.out.sql.entity.BookJpaEntity;
import com.productdock.adapter.out.sql.entity.TopicJpaEntity;
import com.productdock.data.provider.out.kafka.KafkaTestBase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.Duration;
import java.util.concurrent.Callable;

import static com.productdock.data.provider.out.sql.BookEntityMother.defaultBookEntityBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles({"in-memory-db"})
class EditBookReviewApiTest extends KafkaTestBase {

    public static final String TEST_FILE = "testRating.txt";
    public static final String DEFAULT_USER_ID = "::userId::";

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RestRequestProducer requestProducer;

    @BeforeEach
    final void before() {
        reviewRepository.deleteAll();
        bookRepository.deleteAll();
        topicRepository.deleteAll();
    }

    @AfterAll
    static void after() {
        File f = new File(TEST_FILE);
        f.delete();
    }

    @Test
    @WithMockUser
    void editReview_whenUserIdNotValid() throws Exception {
        var book = givenAnyBook();
        var editReviewDtoJson =
                "{\"recommendation\":[]}";
        requestProducer.makeEditBookReviewRequest(editReviewDtoJson, book.getId(), "::wrongId::").andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void editReview_whenReviewIsValid() throws Exception {
        var book = givenAnyBook();
        var reviewDtoJson =
                "{\"comment\":\"::comment::\"," +
                        "\"recommendation\":[\"JUNIOR\",\"MEDIOR\"]}";
        requestProducer.makeBookReviewRequest(reviewDtoJson, book.getId()).andExpect(status().isOk());
        var editReviewDtoJson =
                "{\"comment\":\"::new-comment::\"," +
                        "\"rating\":2," +
                        "\"recommendation\":[\"MEDIOR\",\"SENIOR\"]}";
        requestProducer.makeEditBookReviewRequest(editReviewDtoJson, book.getId(), DEFAULT_USER_ID);

        requestProducer.makeGetBookRequest(book.getId())
                .andExpect(content().json(
                        "{\"id\":" + book.getId() + "," +
                                "\"title\":\"::title::\"," +
                                "\"author\":\"::author::\"," +
                                "\"cover\": \"::cover::\"," +
                                "\"topics\": " + JsonFrom.topicCollection(book.getTopics()) + "," +
                                "\"reviews\": [{\"userFullName\":\"::userFullName::\"," +
                                "\"rating\":2," +
                                "\"recommendation\": [\"MEDIOR\",\"SENIOR\"]," +
                                "\"comment\": \"::new-comment::\"}]}"));

        await()
                .atMost(Duration.ofSeconds(4))
                .until(ifFileExists(TEST_FILE));

        var bookRatingMessage = getBookRatingMessageFrom(TEST_FILE);
        assertThat(bookRatingMessage.bookId).isEqualTo(book.getId());
        assertThat(bookRatingMessage.rating).isEqualTo(2);
        assertThat(bookRatingMessage.ratingsCount).isEqualTo(1);
    }

    @Test
    @WithMockUser
    void returnBadRequest_whenReviewNotExist() throws Exception {
        var book = givenAnyBook();
        var editReviewDtoJson =
                "{\"recommendation\":[]}";
        requestProducer.makeEditBookReviewRequest(editReviewDtoJson, book.getId(), DEFAULT_USER_ID).andExpect(status().isBadRequest());
    }

    private BookJpaEntity givenAnyBook() {
        var marketingTopic = givenTopicWithName("MARKETING");
        var designTopic = givenTopicWithName("DESIGN");
        var book = defaultBookEntityBuilder().topic(marketingTopic).topic(designTopic).build();
        return bookRepository.save(book);
    }


    private Callable<Boolean> ifFileExists(String testFile) {
        return () -> {
            File f = new File(testFile);
            return f.isFile();
        };
    }

    private BookRatingMessage getBookRatingMessageFrom(String testFile) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(testFile);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        var bookRatingMessage = (BookRatingMessage) objectInputStream.readObject();
        objectInputStream.close();
        return bookRatingMessage;
    }

    private TopicJpaEntity givenTopicWithName(String name) {
        var topic = TopicJpaEntity.builder().name(name).build();
        return topicRepository.save(topic);
    }
}
