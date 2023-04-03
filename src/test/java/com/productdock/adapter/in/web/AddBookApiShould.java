package com.productdock.adapter.in.web;

import com.productdock.adapter.in.web.dto.AddBookDto;
import com.productdock.adapter.in.web.mapper.AddBookDtoMapper;
import com.productdock.application.port.in.AddBookUseCase;
import com.productdock.domain.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddBookApiShould {

    @InjectMocks
    private AddBookApi addBookApi;

    @Mock
    private AddBookUseCase addBookUseCase;

    @Mock
    private AddBookDtoMapper addBookDtoMapper;

    private static final Integer DEFAULT_BOOK_COPIES = 1;
    private static final Long DEFAULT_BOOK_ID = 1L;
    @Test
    void createBook() {
        var insertBookDto = mock(AddBookDto.class);
        var book = mock(Book.class);
        insertBookDto.bookCopies = DEFAULT_BOOK_COPIES;

        when(addBookDtoMapper.toDomain(insertBookDto)).thenReturn(book);
        when(addBookUseCase.addBook(book, DEFAULT_BOOK_COPIES)).thenReturn(DEFAULT_BOOK_ID);

        var bookId = addBookApi.createBook(insertBookDto);

        verify(addBookUseCase).addBook(book, DEFAULT_BOOK_COPIES);
        assertThat(bookId).isEqualTo(DEFAULT_BOOK_ID);
    }
}
