package com.productdock.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"test", "in-memory-db"})
class BookApiTestEmulatesHttpCall {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private BookRepository bookRepository;

  @BeforeEach
  final void before() {
    bookRepository.deleteAll();
  }

  @Test
  void getAll_whenBooksExists() throws Exception {
    givenThatBooksAreInDatabase();

    mockMvc.perform(get("/api/books"))
            .andExpect(status().isOk())
            .andExpect(content().json("[{\"id\":1,\"title\":\"Robert C. Martin\",\"author\":\"Clean Architecture\",\"cover\":null}]"));
  }

  private void givenThatBooksAreInDatabase() {
    BookEntity book = new BookEntity();
    book.setAuthor("Clean Architecture");
    book.setTitle("Robert C. Martin");
    bookRepository.save(book);
  }

  @Test
  void getAll_whenNoBooks() throws Exception {
    mockMvc.perform(get("/api/books"))
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));
  }

}