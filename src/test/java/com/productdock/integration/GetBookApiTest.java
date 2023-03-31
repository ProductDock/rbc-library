package com.productdock.integration;

import com.productdock.adapter.out.sql.BookRepository;
import com.productdock.adapter.out.sql.ReviewRepository;
import com.productdock.adapter.out.sql.TopicRepository;
import com.productdock.adapter.out.sql.entity.BookJpaEntity;
import com.productdock.adapter.out.sql.entity.ReviewJpaEntity;
import com.productdock.adapter.out.sql.entity.TopicJpaEntity;
import com.productdock.data.provider.out.kafka.KafkaTestBase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import static com.productdock.data.provider.out.sql.BookEntityMother.defaultBookEntityBuilder;
import static com.productdock.data.provider.out.sql.ReviewJpaEntityMother.defaultReviewEntityBuilder;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles({"in-memory-db"})
class GetBookApiTest extends KafkaTestBase {

    public static final String TEST_FILE = "testRating.txt";
    public static final String FIRST_REVIEWER = "user1";
    public static final String SECOND_REVIEWER = "user2";

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RestRequestProducer requestProducer;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    final void before() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "book_topic", "review", "review", "book", "topic");
    }


    @AfterAll
    static void after() {
        File f = new File(TEST_FILE);
        f.delete();
    }

    @Test
    @WithMockUser
    void getBook_whenIdExistAndNoReviews() throws Exception {
        var book = givenAnyBook();

        requestProducer.makeGetBookRequest(book.getId())
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "{\"id\":" + book.getId() + "," +
                                "\"title\":\"::title::\"," +
                                "\"author\":\"::author::\"," +
                                "\"description\": \"::description::\"," +
                                "\"cover\":\"::cover::\"," +
                                "\"topics\": " + JsonFrom.topicCollection(book.getTopics()) + "," +
                                "\"reviews\": []}"));
    }

    @Test
    @WithMockUser
    void getBook_whenIdExistAndReviewsExist() throws Exception {
        var book = givenAnyBook();
        var calendar = Calendar.getInstance();

        calendar.set(2022, Calendar.APRIL, 5);
        givenReviewForBook(book.getId(), FIRST_REVIEWER, calendar.getTime());
        calendar.set(2022, Calendar.JUNE, 5);
        givenReviewForBook(book.getId(), SECOND_REVIEWER, calendar.getTime());

        requestProducer.makeGetBookRequest(book.getId())
                .andExpect(content().json(
                        "{\"id\":" + book.getId() + "," +
                                "\"title\":\"::title::\"," +
                                "\"author\":\"::author::\"," +
                                "\"cover\":\"::cover::\"," +
                                "\"description\": \"::description::\"," +
                                "\"topics\": " + JsonFrom.topicCollection(book.getTopics()) + "," +
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

    @Test
    @WithMockUser
    void getBookByTitleAndAuthor_whenExist() throws Exception {
        var book = givenAnyBook();

        requestProducer.makeGetBookRequest("::title::", "::author::")
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "{\"id\":" + book.getId() + "," +
                                "\"title\":\"::title::\"," +
                                "\"author\":\"::author::\"," +
                                "\"description\": \"::description::\"," +
                                "\"cover\":\"::cover::\"," +
                                "\"topics\": " + JsonFrom.topicCollection(book.getTopics()) + "," +
                                "\"reviews\": []}"));
    }

    private BookJpaEntity givenAnyBook() {
        var marketingTopic = givenTopicWithName("MARKETING");
        var designTopic = givenTopicWithName("DESIGN");
        var book = defaultBookEntityBuilder().topic(marketingTopic).topic(designTopic).build();
        return bookRepository.save(book);
    }


    private void givenReviewForBook(Long bookId, String reviewerId, Date reviewDate) {
        var review = defaultReviewEntityBuilder()
                .reviewCompositeKey(ReviewJpaEntity.ReviewCompositeKey.builder()
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

    private TopicJpaEntity givenTopicWithName(String name) {
        var topic = TopicJpaEntity.builder().name(name).build();
        return topicRepository.save(topic);
    }
}
