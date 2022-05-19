package com.productdock.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.productdock.book.data.provider.BookEntityMother.*;
import static com.productdock.book.data.provider.ReviewEntityMother.defaultReviewEntityBuilder;
import static com.productdock.book.data.provider.TopicEntityMother.defaultTopic;
import static com.productdock.book.data.provider.TopicEntityMother.defaultTopicBuilder;
import static java.util.Arrays.stream;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"in-memory-db"})
class BookApiTest {

    public static final int RESULTS_PAGE_SIZE = 19;
    public static final String FIRST_PAGE = "0";
    public static final String SECOND_PAGE = "1";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewMapper reviewMapper;

    @BeforeEach
    final void before() {
        reviewRepository.deleteAll();
        bookRepository.deleteAll();
    }

    @Nested
    class SearchWithFilters {

        @Test
        @WithMockUser
        void getSecondPage_whenEmptyResults() throws Exception {
            mockMvc.perform(get("/api/catalog/books")
                            .param("page", SECOND_PAGE)
                            .param("topics", "MARKETING")
                            .param("topics", "DESIGN"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("{\"count\":0,\"books\":[]}"));
        }

        @Test
        @WithMockUser
        void getFirstPage_whenThereAreResults() throws Exception {
            givenABookBelongingToTopic("Title Product", "PRODUCT");
            givenABookBelongingToTopic("Title Marketing", "MARKETING");
            givenABookBelongingToTopic("Title Design", "DESIGN");
            givenABookBelongingToTopic("Title Product & Marketing", "PRODUCT", "MARKETING");

            mockMvc.perform(get("/api/catalog/books")
                            .param("page", FIRST_PAGE)
                            .param("topics", "MARKETING")
                            .param("topics", "DESIGN"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.count").value(3))
                    .andExpect(jsonPath("$.books").value(hasSize(3)))
                    .andExpect(jsonPath("$.books[0].title").value("Title Marketing"))
                    .andExpect(jsonPath("$.books[1].title").value("Title Design"))
                    .andExpect(jsonPath("$.books[2].title").value("Title Product & Marketing"));
        }

        private void givenABookBelongingToTopic(String title, String... topicNames) {
            var topics = createTopicEntitiesWithNames(topicNames);
            var book = defaultBookBuilder().title(title).topics(topics).build();
            bookRepository.save(book);
        }

        private List<TopicEntity> createTopicEntitiesWithNames(String... topicNames) {
            return stream(topicNames)
                    .map(topicName -> TopicEntity.builder().name(topicName).build())
                    .toList();
        }

    }

    @Nested
    class GetBooksWithPagination {

        @Test
        @WithMockUser
        void getFirstPage_whenEmptyResults() throws Exception {
            mockMvc.perform(get("/api/catalog/books").param("page", FIRST_PAGE))
                    .andExpect(status().isOk())
                    .andExpect(content().json("{\"count\":0,\"books\":[]}"));
        }

        @Test
        @WithMockUser
        void getSecondPage_whenThereAreResults() throws Exception {
            givenFirstPageOfResults();
            givenSecondPageOfResults();

            mockMvc.perform(get("/api/catalog/books").param("page", SECOND_PAGE))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.count").value(19))
                    .andExpect(jsonPath("$.books").value(hasSize(1)))
                    .andExpect(jsonPath("$.books[0].id").exists())
                    .andExpect(jsonPath("$.books[0].title").value("Second Page Title"));
        }

        private void givenFirstPageOfResults() {
            for (int i = 0; i < RESULTS_PAGE_SIZE - 1; i++) {
                givenAnyBook();
            }
        }

        private void givenAnyBook() {
            var book = defaultBook();
            bookRepository.save(book);
        }

        private void givenSecondPageOfResults() {
            var book = defaultBookBuilder().title("Second Page Title").build();
            bookRepository.save(book);
        }

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
                                    "\"cover\":\"http://cover\"," +
                                    "\"topics\": [\"MARKETING\",\"DESIGN\"]," +
                                    "\"reviews\": []}"));
        }

        @Test
        @WithMockUser
        void getBook_whenIdExistAndReviewsExist() throws Exception {
            var bookId = givenAnyBook();
            givenReviewForBook(bookId);

            makeGetBookRequest(bookId)
                    .andExpect(content().json(
                            "{\"id\":" + bookId + "," +
                                    "\"title\":\"::title::\"," +
                                    "\"author\":\"::author::\"," +
                                    "\"cover\":\"http://cover\"," +
                                    "\"description\": \"::description::\"," +
                                    "\"topics\": [\"MARKETING\",\"DESIGN\"]," +
                                    "\"reviews\": [{\"userFullName\":\"::userFullName::\"," +
                                    "\"rating\":2," +
                                    "\"recommendation\": [\"JUNIOR\",\"MEDIOR\"]," +
                                    "\"comment\": \"::comment::\"}]," +
                                    "\"rating\":" +
                                    "{\"score\": 2.0," +
                                    "\"count\": 1}}"));
        }

        private Long givenAnyBook() {
            var marketingTopic = givenTopicWithName("MARKETING");
            var designTopic = givenTopicWithName("DESIGN");
            var book = bookWithAnyCover().topic(marketingTopic).topic(designTopic).build();
            return bookRepository.save(book).getId();
        }


        private void givenReviewForBook(Long bookId) {
            var review = defaultReviewEntityBuilder()
                    .reviewCompositeKey(ReviewEntity.ReviewCompositeKey.builder()
                            .bookId(bookId)
                            .userId("::userId::")
                            .build())
                    .userFullName("::userFullName::")
                    .rating((short) 2)
                    .comment("::comment::")
                    .recommendation(3).build();

            reviewRepository.save(review);
        }

    }

    private ResultActions makeGetBookRequest(Long bookId) throws Exception {
        return mockMvc.perform(get("/api/catalog/books/" + bookId)
                        .with(jwt().jwt(jwt -> {
                            jwt.claim("email", "::userId::");
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
                                    "\"cover\": null," +
                                    "\"topics\": [\"MARKETING\",\"DESIGN\"]," +
                                    "\"reviews\": [{\"userFullName\":\"::userFullName::\"," +
                                    "\"rating\":1," +
                                    "\"recommendation\": [\"JUNIOR\",\"MEDIOR\"]," +
                                    "\"comment\": \"::comment::\"}]}"));
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

        private ResultActions makeBookReviewRequest(String reviewDtoJson, Long bookId) throws Exception {
            return mockMvc.perform(post("/api/catalog/books/" + bookId + "/reviews")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(reviewDtoJson)
                    .with(jwt().jwt(jwt -> {
                        jwt.claim("email", "::userId::");
                        jwt.claim("name", "::userFullName::");
                    })));
        }

        private Long givenAnyBook() {
            var marketingTopic = givenTopicWithName("MARKETING");
            var designTopic = givenTopicWithName("DESIGN");
            var book = defaultBookBuilder().topic(marketingTopic).topic(designTopic).build();
            return bookRepository.save(book).getId();
        }

    }

}
