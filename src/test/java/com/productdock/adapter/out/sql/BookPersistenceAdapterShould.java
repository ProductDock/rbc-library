package com.productdock.adapter.out.sql;

import com.productdock.adapter.out.sql.entity.BookJpaEntity;
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
class BookPersistenceAdapterShould {

    private static final Optional<BookJpaEntity> BOOK_ENTITY = Optional.of(mock(BookJpaEntity.class));
    private static final Book BOOK = mock(Book.class);
    private static final Long BOOK_ID = 1L;
    private static final String BOOK_TITLE = "::title::";
    private static final String BOOK_AUTHOR = "::author::";

    @InjectMocks
    private BookPersistenceAdapter bookPersistenceAdapter;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Test
    void findBookWhenIdExist() {
        given(bookRepository.findById(BOOK_ID)).willReturn(BOOK_ENTITY);
        given(bookMapper.toDomain(BOOK_ENTITY.get())).willReturn(BOOK);

        var book = bookPersistenceAdapter.findById(BOOK_ID);

        assertThat(book).isPresent().contains(BOOK);
    }

    @Test
    void findBookWhenIdNotExist() {
        given(bookRepository.findById(BOOK_ID)).willReturn(Optional.empty());

        var book = bookPersistenceAdapter.findById(BOOK_ID);

        assertThat(book).isEmpty();
    }

    @Test
    void findBookByTitleAndAuthorWhenExist() {
        given(bookRepository.findByTitleAndAuthor(BOOK_TITLE, BOOK_AUTHOR)).willReturn(BOOK_ENTITY.get());
        given(bookMapper.toDomain(BOOK_ENTITY.get())).willReturn(BOOK);

        var book = bookPersistenceAdapter.findByTitleAndAuthor(BOOK_TITLE, BOOK_AUTHOR);

        assertThat(book).isPresent().contains(BOOK);
    }

    @Test
    void findBookByTitleAndAuthorNotExist() {
        given(bookRepository.findByTitleAndAuthor(BOOK_TITLE, BOOK_AUTHOR)).willReturn(null);

        var book = bookPersistenceAdapter.findByTitleAndAuthor(BOOK_TITLE, BOOK_AUTHOR);

        assertThat(book).isEmpty();
    }
}
