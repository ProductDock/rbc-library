package com.productdock.book;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;


import static com.productdock.book.data.provider.ReviewMother.defaultReviewDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookApiShould {

    @InjectMocks
    private BookApi bookApi;

    @Mock
    private BookService bookService;

    @Mock
    private ReviewService reviewService;

    public static final long DEFAULT_BOOK_ID = 2;
    public static final String DEFAULT_USER_EMAIL = "test@productdock.com";
    public static final String DEFAULT_USER_FULL_NAME = "test name";

    @Test
    void createReviewForBook() {
        var reviewDto = defaultReviewDto();
        var authenticationMock = mock(Authentication.class);
        var jwtMock = mock(Jwt.class);

        when(authenticationMock.getCredentials()).thenReturn(jwtMock);
        when(jwtMock.getClaim("email")).thenReturn(DEFAULT_USER_EMAIL);
        when(jwtMock.getClaim("name")).thenReturn(DEFAULT_USER_FULL_NAME);

        bookApi.createReviewForBook(DEFAULT_BOOK_ID, reviewDto, authenticationMock);

        verify(reviewService).saveReview(reviewDto);
        assertThat(reviewDto.bookId).isEqualTo(DEFAULT_BOOK_ID);
        assertThat(reviewDto.userId).isEqualTo(DEFAULT_USER_EMAIL);
        assertThat(reviewDto.userFullName).isEqualTo(DEFAULT_USER_FULL_NAME);
    }
}
