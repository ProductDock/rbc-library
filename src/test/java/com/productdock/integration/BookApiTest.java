package com.productdock.integration;

import com.productdock.adapter.out.kafka.BookRatingMessage;
import com.productdock.adapter.out.postresql.BookJpaRepository;
import com.productdock.adapter.out.postresql.ReviewJpaRepository;
import com.productdock.adapter.out.postresql.entity.ReviewEntity;
import com.productdock.adapter.out.postresql.entity.TopicEntity;
import com.productdock.data.provider.KafkaTestBase;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Callable;

import static com.productdock.data.provider.BookEntityMother.defaultBookEntityBuilder;
import static com.productdock.data.provider.ReviewEntityMother.defaultReviewEntityBuilder;
import static com.productdock.data.provider.TopicEntityMother.defaultTopicBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles({"in-memory-db"})
class BookApiTest extends KafkaTestBase {

    public static final String TEST_FILE = "testRating.txt";
    public static final String FIRST_REVIEWER = "user1";
    public static final String SECOND_REVIEWER = "user2";
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

    @Nested
    class GetBookDetails {

        @Test
        @WithMockUser
        void getBook_whenIdExistAndNoReviews() throws Exception {
            var bookId = givenAnyBook();

            mockMvc.perform(get("/api/catalog/books/" + bookId))
                    .andExpect(status().isOk())
                    .andExpect(content().json(
                            "{\"id\":" + bookId + "," +
                                    "\"title\":\"::title::\"," +
                                    "\"author\":\"::author::\"," +
                                    "\"description\": \"::description::\"," +
                                    "\"cover\":\"::cover::\"," +
                                    "\"topics\": [\"MARKETING\",\"DESIGN\"]," +
                                    "\"reviews\": []}"));
        }

        @Test
        @WithMockUser
        void getBook_whenIdExistAndReviewsExist() throws Exception {
            var bookId = givenAnyBook();
            var calendar = Calendar.getInstance();

            calendar.set(2022, Calendar.APRIL, 5);
            givenReviewForBook(bookId, FIRST_REVIEWER, calendar.getTime());
            calendar.set(2022, Calendar.JUNE, 5);
            givenReviewForBook(bookId, SECOND_REVIEWER, calendar.getTime());

            makeGetBookRequest(bookId)
                    .andExpect(content().json(
                            "{\"id\":" + bookId + "," +
                                    "\"title\":\"::title::\"," +
                                    "\"author\":\"::author::\"," +
                                    "\"cover\":\"::cover::\"," +
                                    "\"description\": \"::description::\"," +
                                    "\"topics\": [\"MARKETING\",\"DESIGN\"]," +
                                    "\"reviews\": [{\"userFullName\":\"::userFullName::\"," +
                                    "\"userId\":\"" + SECOND_REVIEWER + "\"," +
                                    "\"rating\":2," +
                                    "\"recommendation\": [\"JUNIOR\",\"MEDIOR\"]," +
                                    "\"comment\": \"::comment::\"}," +
                                    "{\"userFullName\":\"::userFullName::\"," +
                                    "\"userId\":\"" + FIRST_REVIEWER + "\"," +
                                    "\"rating\":2," +
                                    "\"recommendation\": [\"JUNIOR\",\"MEDIOR\"]," +
                                    "\"comment\": \"::comment::\"}]," +
                                    "\"rating\":" +
                                    "{\"score\": 2.0," +
                                    "\"count\": 2}}"))
                    .andExpect(jsonPath("$.reviews[0].userId", is(SECOND_REVIEWER)));
        }

        private Long givenAnyBook() {
            var marketingTopic = givenTopicWithName("MARKETING");
            var designTopic = givenTopicWithName("DESIGN");
            var book = defaultBookEntityBuilder().topic(marketingTopic).topic(designTopic).build();
            return bookRepository.save(book).getId();
        }


        private void givenReviewForBook(Long bookId, String reviewerId, Date reviewDate) {
            var review = defaultReviewEntityBuilder()
                    .reviewCompositeKey(ReviewEntity.ReviewCompositeKey.builder()
                            .bookId(bookId)
                            .userId(reviewerId)
                            .build())
                    .userFullName("::userFullName::")
                    .rating((short) 2)
                    .comment("::comment::")
                    .date(reviewDate)
                    .recommendation(3).build();

            reviewRepository.save(review);
        }

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
        return defaultTopicBuilder().name(name).build();
    }

    @Nested
    class CreateReviewForBook {

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

    }

    @Nested
    class EditBookReview {

        @Test
        @WithMockUser
        void editReview_whenUserIdNotValid() throws Exception {
            var bookId = givenAnyBook();
            var editReviewDtoJson =
                    "{\"recommendation\":[]}";
            makeEditBookReviewRequest(editReviewDtoJson, bookId, "::wrongId::").andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser
        void editReview_whenReviewIsValid() throws Exception {
            var bookId = givenAnyBook();
            var reviewDtoJson =
                    "{\"comment\":\"::comment::\"," +
                            "\"recommendation\":[\"JUNIOR\",\"MEDIOR\"]}";
            makeBookReviewRequest(reviewDtoJson, bookId).andExpect(status().isOk());
            var editReviewDtoJson =
                    "{\"comment\":\"::new-comment::\"," +
                            "\"rating\":2," +
                            "\"recommendation\":[\"MEDIOR\",\"SENIOR\"]}";
            makeEditBookReviewRequest(editReviewDtoJson, bookId, DEFAULT_USER_ID);

            makeGetBookRequest(bookId)
                    .andExpect(content().json(
                            "{\"id\":" + bookId + "," +
                                    "\"title\":\"::title::\"," +
                                    "\"author\":\"::author::\"," +
                                    "\"cover\": \"::cover::\"," +
                                    "\"topics\": [\"MARKETING\",\"DESIGN\"]," +
                                    "\"reviews\": [{\"userFullName\":\"::userFullName::\"," +
                                    "\"rating\":2," +
                                    "\"recommendation\": [\"MEDIOR\",\"SENIOR\"]," +
                                    "\"comment\": \"::new-comment::\"}]}"));

            await()
                    .atMost(Duration.ofSeconds(4))
                    .until(ifFileExists(TEST_FILE));

            var bookRatingMessage = getBookRatingMessageFrom(TEST_FILE);
            assertThat(bookRatingMessage.getBookId()).isEqualTo(bookId);
            assertThat(bookRatingMessage.getRating()).isEqualTo(2);
            assertThat(bookRatingMessage.getRatingsCount()).isEqualTo(1);
        }

        @Test
        @WithMockUser
        void returnBadRequest_whenReviewNotExist() throws Exception {
            var bookId = givenAnyBook();
            var editReviewDtoJson =
                    "{\"recommendation\":[]}";
            makeEditBookReviewRequest(editReviewDtoJson, bookId, DEFAULT_USER_ID).andExpect(status().isBadRequest());
        }

        private Long givenAnyBook() {
            var marketingTopic = givenTopicWithName("MARKETING");
            var designTopic = givenTopicWithName("DESIGN");
            var book = defaultBookEntityBuilder().topic(marketingTopic).topic(designTopic).build();
            return bookRepository.save(book).getId();
        }

        private ResultActions makeEditBookReviewRequest(String reviewDtoJson, Long bookId, String userId) throws Exception {
            return mockMvc.perform(put("/api/catalog/books/" + bookId + "/reviews/" + userId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(reviewDtoJson)
                    .with(jwt().jwt(jwt -> {
                        jwt.claim("email", DEFAULT_USER_ID);
                        jwt.claim("name", "::userFullName::");
                    })));
        }
    }

    @Nested
    class DeleteBookReview {

        @Test
        @WithMockUser
        void deleteReview_whenUserIdNotValid() throws Exception {
            var bookId = givenAnyBook();
            makeDeleteBookReviewRequest(bookId, "::wrongId::").andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser
        void deleteReview_whenReviewIsValid() throws Exception {
            var bookId = givenAnyBook();
            var reviewDtoJson =
                    "{\"comment\":\"::comment::\"," +
                            "\"rating\":3," +
                            "\"recommendation\":[\"JUNIOR\",\"MEDIOR\"]}";
            makeBookReviewRequest(reviewDtoJson, bookId).andExpect(status().isOk());
            makeDeleteBookReviewRequest(bookId, DEFAULT_USER_ID).andExpect(status().isOk());
            await()
                    .atMost(Duration.ofSeconds(4))
                    .until(ifFileExists(TEST_FILE));

            var bookRatingMessage = getBookRatingMessageFrom(TEST_FILE);
            assertThat(bookRatingMessage.getBookId()).isEqualTo(bookId);
            assertThat(bookRatingMessage.getRating()).isNull();
            assertThat(bookRatingMessage.getRatingsCount()).isZero();
        }

        @Test
        @WithMockUser
        void returnBadRequest_whenReviewNotExist() throws Exception {
            var bookId = givenAnyBook();
            makeDeleteBookReviewRequest(bookId, DEFAULT_USER_ID).andExpect(status().isBadRequest());
        }

        private Long givenAnyBook() {
            var marketingTopic = givenTopicWithName("MARKETING");
            var designTopic = givenTopicWithName("DESIGN");
            var book = defaultBookEntityBuilder().topic(marketingTopic).topic(designTopic).build();
            return bookRepository.save(book).getId();
        }

        private ResultActions makeDeleteBookReviewRequest(Long bookId, String userId) throws Exception {
            return mockMvc.perform(delete("/api/catalog/books/" + bookId + "/reviews/" + userId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(jwt().jwt(jwt -> {
                        jwt.claim("email", DEFAULT_USER_ID);
                        jwt.claim("name", "::userFullName::");
                    })));
        }
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

}
