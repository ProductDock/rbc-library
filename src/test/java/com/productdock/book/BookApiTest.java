package com.productdock.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.productdock.book.data.provider.BookEntityMother.defaultBook;
import static com.productdock.book.data.provider.BookEntityMother.defaultBookBuilder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"in-memory-db"})
class BookApiTest {

    public static final int RESULTS_PAGE_SIZE = 19;
    public static final String FIRST_PAGE = "0";
    public static final String SECOND_PAGE = "1";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    final void before() {
        bookRepository.deleteAll();
    }

    @Test
    @WithMockUser
    void countAllBooks() throws Exception {
        givenAnyBook();

        mockMvc.perform(get("/api/books/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    private void givenAnyBook() {
        var book = defaultBook();
        bookRepository.save(book);
    }

    @Nested
    class SearchWithFilters {

        @Test
        @WithMockUser
        void getSecondPage_whenEmptyResults() throws Exception {
            mockMvc.perform(get("/api/books")
                            .param("page", SECOND_PAGE)
                            .param("topics", "MARKETING")
                            .param("topics", "DESIGN"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("{\"count\":0,\"books\":[]}"));
        }

        @Test
        @WithMockUser
        void getFirstPage_whenThereAreResults() throws Exception {
            givenABookBelongingToTopic("PRODUCT", "Title Product");
            givenABookBelongingToTopic("MARKETING", "Title Marketing");
            givenABookBelongingToTopic("DESIGN", "Title Design");

            mockMvc.perform(get("/api/books")
                            .param("page", FIRST_PAGE)
                            .param("topics", "MARKETING")
                            .param("topics", "DESIGN"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.count").value(2))
                    .andExpect(jsonPath("$.books").value(hasSize(2)))
                    .andExpect(jsonPath("$.books[0].title").value("Title Marketing"))
                    .andExpect(jsonPath("$.books[1].title").value("Title Design"));
        }

        private void givenABookBelongingToTopic(String topicName, String title) {
            var topic = new TopicEntity();
            topic.setName(topicName);
            var book = defaultBookBuilder().title(title).topic(topic).build();

            bookRepository.save(book);
        }

    }

    @Nested
    class GetBooksWithPagination {

        @Test
        @WithMockUser
        void getFirstPage_whenEmptyResults() throws Exception {
            mockMvc.perform(get("/api/books").param("page", FIRST_PAGE))
                    .andExpect(status().isOk())
                    .andExpect(content().json("{\"count\":0,\"books\":[]}"));
        }

        @Test
        @WithMockUser
        void getSecondPage_whenThereAreResults() throws Exception {
            givenFirstPageOfResults();
            givenSecondPageOfResults();

            mockMvc.perform(get("/api/books").param("page", SECOND_PAGE))
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

        private void givenSecondPageOfResults() {
            var book = defaultBookBuilder().title("Second Page Title").build();
            bookRepository.save(book);
        }

    }

}
