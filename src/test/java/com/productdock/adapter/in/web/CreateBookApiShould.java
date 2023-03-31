package com.productdock.adapter.in.web;

import com.productdock.adapter.in.web.dto.InsertBookDto;
import com.productdock.adapter.in.web.mapper.InsertBookDtoMapper;
import com.productdock.application.port.in.SaveBookUseCase;
import com.productdock.domain.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateBookApiShould {

    @InjectMocks
    private CreateBookApi createBookApi;

    @Mock
    private SaveBookUseCase saveBookUseCase;

    @Mock
    private InsertBookDtoMapper insertBookDtoMapper;

    private static final Integer DEFAULT_BOOK_COPIES = 1;
    private static final Long DEFAULT_BOOK_ID = 1L;
    @Test
    void createBook() {
        var insertBookDto = mock(InsertBookDto.class);
        var book = mock(Book.class);
        insertBookDto.bookCopies = DEFAULT_BOOK_COPIES;

        when(insertBookDtoMapper.toDomain(insertBookDto)).thenReturn(book);
        when(saveBookUseCase.saveBook(book, DEFAULT_BOOK_COPIES)).thenReturn(DEFAULT_BOOK_ID);

        var bookId = createBookApi.createBook(insertBookDto);

        verify(saveBookUseCase).saveBook(book, DEFAULT_BOOK_COPIES);
        assertThat(bookId).isEqualTo(DEFAULT_BOOK_ID);
    }
}
