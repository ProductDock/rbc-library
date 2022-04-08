package com.productdock.book;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class BookServiceShould {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Test
    void getBooksWithNullTopics() {
        var firstPage = PageRequest.of(0, 18);
        given(bookRepository.findAll(firstPage)).willReturn(new PageImpl<>(aBookCollection()));

        var books = bookService.getBooks(null, 0);

        assertThat(books).hasSize(2);
    }

    private List<BookEntity> aBookCollection() {
        return of(
                mock(BookEntity.class),
                mock(BookEntity.class))
                .collect(toList());
    }

    @Test
    void getBooksWithEmptyTopics() {
        var firstPage = PageRequest.of(0, 18);
        given(bookRepository.findAll(firstPage)).willReturn(new PageImpl<>(aBookCollection()));

        var books = bookService.getBooks(emptyList(), 0);

        assertThat(books).hasSize(2);
    }

    @Test
    void getBooksByTopics() {
        var firstPage = PageRequest.of(0, 18);
        List<String> topics = List.of("MARKETING", "DESIGN");
        given(bookRepository.findAllByTopicsName(topics, firstPage)).willReturn(new PageImpl<>(aBookCollection()));

        var books = bookService.getBooks(topics, 0);

        assertThat(books).hasSize(2);
    }

    @Test
    void countAllBooks() {
        given(bookRepository.count()).willReturn(2L);

        var bookCount = bookService.countAllBooks();

        assertThat(bookCount).isEqualTo(2L);
    }
}
