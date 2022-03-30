package com.productdock.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"in-memory-db"})
class BookApiTest {

    public static final int NUMBER_OF_BOOKS = 19;
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
    void getSecondPage_whenBooksExists() throws Exception {
        givenFirstPageOfResults();
        givenSecondPageOfResults();

        mockMvc.perform(get("/api/books").param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\": " + NUMBER_OF_BOOKS + ",\"title\":\"Second Page Title\",\"author\":\"Second Page Author\",\"cover\":null}]"));
    }

    private void givenSecondPageOfResults() {
        BookEntity book = new BookEntity();
        book.setAuthor("Second Page Author");
        book.setTitle("Second Page Title");
        bookRepository.save(book);
    }

    private void givenFirstPageOfResults() {
        for (int i = 0; i < NUMBER_OF_BOOKS - 1; i++) {
            givenThatBookIsInDatabase();
        }
    }

    private void givenThatBookIsInDatabase() {
        BookEntity book = new BookEntity();
        book.setAuthor("Clean Architecture");
        book.setTitle("Robert C. Martin");
        bookRepository.save(book);
    }

    @Test
    @WithMockUser
    void getAll_whenNoBooks() throws Exception {
        mockMvc.perform(get("/api/books").param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @WithMockUser
    void countAllBooks_whenBooksExist() throws Exception {
        givenThatBookIsInDatabase();

        mockMvc.perform(get("/api/books/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }
}
