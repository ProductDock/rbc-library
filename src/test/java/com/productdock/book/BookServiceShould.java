package com.productdock.book;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        //Given
        given(bookRepository.findAll()).willReturn(aBookCollection());

        //When
        List<BookDto> books = bookService.getAll();

        //Then
        assertThat(books).hasSize(2);
    }

    private List<BookEntity> aBookCollection() {
        return of(
                mock(BookEntity.class),
                mock(BookEntity.class))
                .collect(toList());
    }
}