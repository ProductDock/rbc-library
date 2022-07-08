package com.productdock.adapter.out.sql;

import com.productdock.adapter.out.sql.entity.BookEntity;
import com.productdock.adapter.out.sql.mapper.BookMapper;
import com.productdock.domain.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class BookRepositoryShould {

    private static final Optional<BookEntity> BOOK_ENTITY = Optional.of(mock(BookEntity.class));
    private static final Book BOOK = mock(Book.class);
    private static final Long BOOK_ID = 1L;

    @InjectMocks
    private BookRepository bookRepository;

    @Mock
    private BookJpaRepository bookJpaRepository;

    @Mock
    private BookMapper bookMapper;

    @Test
    void findBookWhenIdExist() {
        given(bookJpaRepository.findById(BOOK_ID)).willReturn(BOOK_ENTITY);
        given(bookMapper.toDomain(BOOK_ENTITY.get())).willReturn(BOOK);

        var book = bookRepository.findById(BOOK_ID);

        assertThat(book).isPresent();
    }

    @Test
    void findBookWhenIdNotExist() {
        given(bookJpaRepository.findById(BOOK_ID)).willReturn(Optional.empty());

        var book = bookRepository.findById(BOOK_ID);

        assertThat(book).isEmpty();
    }
}
