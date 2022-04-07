package com.productdock.book;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

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
    void getAllBooks() {
        Pageable firstPage = PageRequest.of(0, 18);
        given(bookRepository.findAll(firstPage)).willReturn(new PageImpl<>(aBookCollection()));

        List<BookDto> books = bookService.getBooks(null, 0);

        assertThat(books).hasSize(2);
    }

    private List<BookEntity> aBookCollection() {
        return of(
                mock(BookEntity.class),
                mock(BookEntity.class))
                .collect(toList());
    }

    @Test
    void countAllBooks() {
        given(bookRepository.count()).willReturn(2L);

        long bookCount = bookService.countAllBooks();

        assertThat(bookCount).isEqualTo(2L);
    }
}
