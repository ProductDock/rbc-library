package com.productdock.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class BookApiTestWithWebServer {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private BookRepository bookRepository;

  @BeforeEach
  final void before() {
    bookRepository.deleteAll();
  }

  @Test
  void getAll_whenBooksExists() {
    givenThatBooksAreInDatabase();

    String booksUrl = "http://localhost:" + port + "/api/books";
    String actualResponse = this.restTemplate.getForObject(booksUrl, String.class);

    assertThat(actualResponse)
            .isEqualTo("[{\"id\":1,\"title\":\"Robert C. Martin\",\"author\":\"Clean Architecture\",\"cover\":null}]");
  }

  @Test
  void getAll_whenNoBooks() {
    String booksUrl = "http://localhost:" + port + "/api/books";
    String actualResponse = this.restTemplate.getForObject(booksUrl, String.class);

    assertThat(actualResponse).isEqualTo("[]");
  }

  private void givenThatBooksAreInDatabase() {
    BookEntity book = new BookEntity();
    book.setAuthor("Clean Architecture");
    book.setTitle("Robert C. Martin");
    bookRepository.save(book);
  }

}