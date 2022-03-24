package com.productdock.book;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"mock-db"})
class BookApiTestEmulatesHttpCallAndMockDb {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private BookRepository bookRepository;

  @Test
  void getAll_whenBooksExists() throws Exception {
    givenThatBooksExist();

    mockMvc.perform(get("/api/books"))
            .andExpect(status().isOk())
            .andExpect(content().json("[{\"id\":1,\"title\":\"Robert C. Martin\",\"author\":\"Clean Architecture\",\"cover\":null}]"));
  }

  private void givenThatBooksExist() {
    List<BookEntity> books = aBooksCollection();
    BDDMockito.given(bookRepository.findAll()).willReturn(books);
  }

  private List<BookEntity> aBooksCollection() {
    BookEntity book = new BookEntity();
    book.setId(1L);
    book.setAuthor("Clean Architecture");
    book.setTitle("Robert C. Martin");
    return of(book).collect(toList());
  }

  @Test
  void getAll_whenNoBooks() throws Exception {
    mockMvc.perform(get("/api/books"))
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));
  }

}