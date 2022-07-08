package com.productdock.integration;

import com.productdock.adapter.out.sql.BookJpaRepository;
import com.productdock.adapter.out.sql.ReviewJpaRepository;
import com.productdock.adapter.out.sql.entity.ReviewEntity;
import com.productdock.adapter.out.sql.entity.TopicEntity;
import com.productdock.data.provider.out.kafka.KafkaTestBase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import static com.productdock.data.provider.out.postgresql.BookEntityMother.defaultBookEntityBuilder;
import static com.productdock.data.provider.out.postgresql.ReviewEntityMother.defaultReviewEntityBuilder;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles({"in-memory-db"})
class GetBookApiTest extends KafkaTestBase {

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
