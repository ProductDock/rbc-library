package com.productdock.integration;

import com.productdock.adapter.out.kafka.BookRatingMessage;
import com.productdock.adapter.out.sql.BookJpaRepository;
import com.productdock.adapter.out.sql.ReviewJpaRepository;
import com.productdock.adapter.out.sql.entity.TopicEntity;
import com.productdock.data.provider.out.kafka.KafkaTestBase;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.Duration;
import java.util.concurrent.Callable;

import static com.productdock.data.provider.out.postgresql.BookEntityMother.defaultBookEntityBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles({"in-memory-db"})
class CreateBookReviewApiTest extends KafkaTestBase {

    public static final String TEST_FILE = "testRating.txt";
    public static final String DEFAULT_USER_ID = "::userId::";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookJpaRepository bookRepository;

    @Autowired
    private ReviewJpaRepository reviewRepository;

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
        makeBookReviewRequest(reviewDtoJson, bookId).andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void createReview_whenReviewIsValid() throws Exception {
        var bookId = givenAnyBook();
        var reviewDtoJson =
                "{\"comment\":\"::comment::\"," +
                        "\"rating\":1," +
                        "\"recommendation\":[\"JUNIOR\",\"MEDIOR\"]}";
        makeBookReviewRequest(reviewDtoJson, bookId).andExpect(status().isOk());
        makeGetBookRequest(bookId)
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
        makeBookReviewRequest(reviewDtoJson, bookId).andExpect(status().isOk());
        makeBookReviewRequest(reviewDtoJson, bookId).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void returnBadRequest_whenReviewHasTooLongComment() throws Exception {
        var bookId = givenAnyBook();
        var reviewDtoJson =
                "{\"comment\":\"" + RandomString.make(501) + "\"," +
                        "\"rating\":null," +
                        "\"recommendation\":[]}";
        makeBookReviewRequest(reviewDtoJson, bookId).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void returnBadRequest_whenReviewHasInvalidRating() throws Exception {
        var bookId = givenAnyBook();
        var reviewDtoJson =
                "{\"comment\":\"::comment::\"," +
                        "\"rating\":7," +
                        "\"recommendation\":[]}";
        makeBookReviewRequest(reviewDtoJson, bookId).andExpect(status().isBadRequest());
    }

    private Long givenAnyBook() {
        var marketingTopic = givenTopicWithName("MARKETING");
        var designTopic = givenTopicWithName("DESIGN");
        var book = defaultBookEntityBuilder().topic(marketingTopic).topic(designTopic).build();
        return bookRepository.save(book).getId();
    }

    private ResultActions makeBookReviewRequest(String reviewDtoJson, Long bookId) throws Exception {
        return mockMvc.perform(post("/api/catalog/books/" + bookId + "/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reviewDtoJson)
                .with(jwt().jwt(jwt -> {
                    jwt.claim("email", DEFAULT_USER_ID);
                    jwt.claim("name", "::userFullName::");
                })));
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

    private ResultActions makeGetBookRequest(Long bookId) throws Exception {
        return mockMvc.perform(get("/api/catalog/books/" + bookId)
                        .with(jwt().jwt(jwt -> {
                            jwt.claim("email", DEFAULT_USER_ID);
                            jwt.claim("name", "::userFullName::");
                        })))
                .andExpect(status().isOk());
    }

    private TopicEntity givenTopicWithName(String name) {
        return TopicEntity.builder().name(name).build();
    }
}
