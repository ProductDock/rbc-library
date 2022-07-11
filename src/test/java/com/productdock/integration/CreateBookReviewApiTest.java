package com.productdock.integration;

import com.productdock.adapter.out.kafka.BookRatingMessage;
import com.productdock.adapter.out.sql.BookJpaRepository;
import com.productdock.adapter.out.sql.ReviewJpaRepository;
import com.productdock.adapter.out.sql.entity.TopicJpaEntity;
import com.productdock.data.provider.out.kafka.KafkaTestBase;
import net.bytebuddy.utility.RandomString;
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

import static com.productdock.data.provider.out.postgresql.BookEntityMother.defaultBookEntityBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles({"in-memory-db"})
class CreateBookReviewApiTest extends KafkaTestBase {

    public static final String TEST_FILE = "testRating.txt";

    @Autowired
    private BookJpaRepository bookRepository;

    @Autowired
    private ReviewJpaRepository reviewRepository;

    @Autowired
    private RequestProducer requestProducer;

    @BeforeEach
    final void before() {
        reviewRepository.deleteAll();
        bookRepository.deleteAll();
    }

    @AfterAll
    static void after() {
        File f = new File(TEST_FILE);
        f.delete();
    }

    @Test
    @WithMockUser
    void createReview_whenCommentAndRatingMissing() throws Exception {
        var bookId = givenAnyBook();
        var reviewDtoJson =
                "{\"recommendation\":[]}";
        requestProducer.makeBookReviewRequest(reviewDtoJson, bookId).andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void createReview_whenReviewIsValid() throws Exception {
        var bookId = givenAnyBook();
        var reviewDtoJson =
                "{\"comment\":\"::comment::\"," +
                        "\"rating\":1," +
                        "\"recommendation\":[\"JUNIOR\",\"MEDIOR\"]}";
        requestProducer.makeBookReviewRequest(reviewDtoJson, bookId).andExpect(status().isOk());
        requestProducer.makeGetBookRequest(bookId)
                .andExpect(content().json(
                        "{\"id\":" + bookId + "," +
                                "\"title\":\"::title::\"," +
                                "\"author\":\"::author::\"," +
                                "\"cover\": \"::cover::\"," +
                                "\"topics\": [\"MARKETING\",\"DESIGN\"]," +
                                "\"reviews\": [{\"userFullName\":\"::userFullName::\"," +
                                "\"rating\":1," +
                                "\"recommendation\": [\"JUNIOR\",\"MEDIOR\"]," +
                                "\"comment\": \"::comment::\"}]}"));

        await()
                .atMost(Duration.ofSeconds(4))
                .until(ifFileExists(TEST_FILE));

        var bookRatingMessage = getBookRatingMessageFrom(TEST_FILE);
        assertThat(bookRatingMessage.getBookId()).isEqualTo(bookId);
        assertThat(bookRatingMessage.getRating()).isEqualTo(1);
        assertThat(bookRatingMessage.getRatingsCount()).isEqualTo(1);
    }

    @Test
    @WithMockUser
    void returnBadRequest_whenReviewAlreadyExists() throws Exception {
        var bookId = givenAnyBook();
        var reviewDtoJson =
                "{\"comment\":\"::comment::\"," +
                        "\"rating\":null," +
                        "\"recommendation\":[]}";
        requestProducer.makeBookReviewRequest(reviewDtoJson, bookId).andExpect(status().isOk());
        requestProducer.makeBookReviewRequest(reviewDtoJson, bookId).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void returnBadRequest_whenReviewHasTooLongComment() throws Exception {
        var bookId = givenAnyBook();
        var reviewDtoJson =
                "{\"comment\":\"" + RandomString.make(501) + "\"," +
                        "\"rating\":null," +
                        "\"recommendation\":[]}";
        requestProducer.makeBookReviewRequest(reviewDtoJson, bookId).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void returnBadRequest_whenReviewHasInvalidRating() throws Exception {
        var bookId = givenAnyBook();
        var reviewDtoJson =
                "{\"comment\":\"::comment::\"," +
                        "\"rating\":7," +
                        "\"recommendation\":[]}";
        requestProducer.makeBookReviewRequest(reviewDtoJson, bookId).andExpect(status().isBadRequest());
    }

    private Long givenAnyBook() {
        var marketingTopic = givenTopicWithName("MARKETING");
        var designTopic = givenTopicWithName("DESIGN");
        var book = defaultBookEntityBuilder().topic(marketingTopic).topic(designTopic).build();
        return bookRepository.save(book).getId();
    }

    private Callable<Boolean> ifFileExists(String testFile) {
        Callable<Boolean> checkForFile = () -> {
            File f = new File(testFile);
            return f.isFile();
        };
        return checkForFile;
    }

    private BookRatingMessage getBookRatingMessageFrom(String testFile) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(testFile);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        var bookRatingMessage = (BookRatingMessage) objectInputStream.readObject();
        objectInputStream.close();
        return bookRatingMessage;
    }

    private TopicJpaEntity givenTopicWithName(String name) {
        return TopicJpaEntity.builder().name(name).build();
    }
}
