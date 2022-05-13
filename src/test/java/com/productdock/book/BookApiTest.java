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

import static com.productdock.book.data.provider.BookEntityMother.*;

import java.util.List;

import static com.productdock.book.data.provider.BookEntityMother.defaultBook;
import static com.productdock.book.data.provider.BookEntityMother.defaultBookBuilder;
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
        bookRepository.deleteAll();
        reviewRepository.deleteAll();
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
        void getBook_whenTheIdIsExisting() throws Exception {
            var book = bookWithAnyCover().id(1L).build();
            bookRepository.save(book);

            mockMvc.perform(get("/api/catalog/books/1").param("bookId", "1"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("{\"id\":1,\"title\":\"::title::\", \"author\":\"::author::\",\"cover\":\"http://cover\"}"));
        }

    }

    @Nested
    class CreateReviewForBook {

        @Test
        @WithMockUser
        void createReview_whenReviewIsValid() throws Exception {
            var reviewDtoJson =
                    "{\"bookId\":null," +
                    "\"userId\":\"::userId::\"," +
                    "\"userFullName\":\"::userFullName::\"," +
                    "\"comment\":\"::comment::\"," +
                    "\"rating\":null," +
                    "\"recommendation\":[]}";
            makeBookReviewRequest(reviewDtoJson).andExpect(status().isOk());
        }

        @Test
        @WithMockUser
        void returnBadRequest_whenReviewAlreadyExists() throws Exception {
            var reviewDtoJson =
                    "{\"bookId\":1," +
                    "\"userId\":\"::userId::\"," +
                    "\"userFullName\":\"::userFullName::\"," +
                    "\"comment\":\"::comment::\"," +
                    "\"rating\":null," +
                    "\"recommendation\":[]}";
            makeBookReviewRequest(reviewDtoJson).andExpect(status().isOk());
            makeBookReviewRequest(reviewDtoJson).andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser
        void returnBadRequest_whenReviewHasTooLongComment() throws Exception {
            var reviewDtoJson =
                    "{\"bookId\":1," +
                    "\"userId\":\"::userId::\"," +
                    "\"userFullName\":\"::userFullName::\"," +
                    "\"comment\":\""+ RandomString.make(501) + "\"," +
                    "\"rating\":null," +
                    "\"recommendation\":[]}";
            makeBookReviewRequest(reviewDtoJson).andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser
        void returnBadRequest_whenReviewHasInvalidRating() throws Exception {
            var reviewDtoJson =
                    "{\"bookId\":1," +
                    "\"userId\":\"::userId::\"," +
                    "\"userFullName\":\"::userFullName::\"," +
                    "\"comment\":\"::comment::\"," +
                    "\"rating\":7," +
                    "\"recommendation\":[]}";
            makeBookReviewRequest(reviewDtoJson).andExpect(status().isBadRequest());
        }

        private ResultActions makeBookReviewRequest(String reviewDtoJson) throws Exception {
            return mockMvc.perform(post("/api/catalog/books/1/reviews")
                    .param("bookId", "1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(reviewDtoJson)
                    .with(jwt().jwt(jwt -> {
                        jwt.claim("email", "::userId::");
                        jwt.claim("name", "::userFullName::");
                    })));
        }
    }

}
