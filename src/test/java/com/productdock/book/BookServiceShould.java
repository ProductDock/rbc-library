package com.productdock.book;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

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
        when(bookRepository.findAll()).thenReturn(getBookEntities());

        //When
        List<BookDto> books = bookService.getAll();

        //Then
        assertThat(books.size()).isEqualTo(1);
    }

    private List<BookEntity> getBookEntities() {
        BookEntity entity = new BookEntity();
        entity.setId(1L);
        entity.setAuthor("Ivo Andric");
        entity.setTitle("Prokleta Avlija");

        List<BookEntity> list = new ArrayList<>();
        list.add(entity);
        return list;
    }
}