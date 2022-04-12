package com.productdock.book;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

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
    void getBooksByTopics() {
        var topicsFilter = Optional.of(List.of("TOPIC"));
        var firstPage = PageRequest.of(0, 18);
        given(bookRepository.findByTopicsName(topicsFilter, firstPage)).willReturn(aBooksPage());

        var books = bookService.getBooks(topicsFilter, 0);

        assertThat(books.count).isEqualTo(2);
        assertThat(books.books).hasSize(2);
    }

    private Page<BookEntity> aBooksPage() {
        List<BookEntity> bookEntities = of(
                mock(BookEntity.class),
                mock(BookEntity.class))
                .collect(toList());
        return new PageImpl<>(bookEntities);
    }

}
