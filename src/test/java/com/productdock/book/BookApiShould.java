package com.productdock.book;

import com.productdock.exception.ForbiddenAccessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import static com.productdock.book.data.provider.ReviewDtoMother.defaultReviewDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    public static final String WRONG_USER_ID_EXCEPTION_MESSAGE = "You don't have access for resource";

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

    @Test
    void editReviewForBook() {
        var reviewDto = defaultReviewDto();
        var authenticationMock = mock(Authentication.class);
        var jwtMock = mock(Jwt.class);

        when(authenticationMock.getCredentials()).thenReturn(jwtMock);
        when(jwtMock.getClaim("email")).thenReturn(DEFAULT_USER_EMAIL);
        when(jwtMock.getClaim("name")).thenReturn(DEFAULT_USER_FULL_NAME);

        bookApi.editReviewForBook(DEFAULT_BOOK_ID, DEFAULT_USER_EMAIL, reviewDto, authenticationMock);

        verify(reviewService).editReview(reviewDto);
        assertThat(reviewDto.bookId).isEqualTo(DEFAULT_BOOK_ID);
        assertThat(reviewDto.userId).isEqualTo(DEFAULT_USER_EMAIL);
        assertThat(reviewDto.userFullName).isEqualTo(DEFAULT_USER_FULL_NAME);
    }

    @Test
    void editReviewForBook_whenWrongUserId() {
        var reviewDto = defaultReviewDto();
        var authenticationMock = mock(Authentication.class);
        var jwtMock = mock(Jwt.class);

        when(authenticationMock.getCredentials()).thenReturn(jwtMock);
        when(jwtMock.getClaim("email")).thenReturn(DEFAULT_USER_EMAIL);

        assertThatThrownBy(() -> bookApi.editReviewForBook(DEFAULT_BOOK_ID, "::wrongId::", reviewDto, authenticationMock))
                .isInstanceOf(ForbiddenAccessException.class)
                .hasMessage(WRONG_USER_ID_EXCEPTION_MESSAGE);
    }

    @Test
    void deleteReviewForBook() {
        var authenticationMock = mock(Authentication.class);
        var jwtMock = mock(Jwt.class);

        when(authenticationMock.getCredentials()).thenReturn(jwtMock);
        when(jwtMock.getClaim("email")).thenReturn(DEFAULT_USER_EMAIL);

        bookApi.deleteReviewForBook(DEFAULT_BOOK_ID, DEFAULT_USER_EMAIL, authenticationMock);

        verify(reviewService).deleteReview(DEFAULT_BOOK_ID, DEFAULT_USER_EMAIL);
    }

    @Test
    void deleteReviewForBook_whenWrongUserId() {
        var authenticationMock = mock(Authentication.class);
        var jwtMock = mock(Jwt.class);

        when(authenticationMock.getCredentials()).thenReturn(jwtMock);
        when(jwtMock.getClaim("email")).thenReturn(DEFAULT_USER_EMAIL);

        assertThatThrownBy(() -> bookApi.deleteReviewForBook(DEFAULT_BOOK_ID, "::wrongId::", authenticationMock))
                .isInstanceOf(ForbiddenAccessException.class)
                .hasMessage(WRONG_USER_ID_EXCEPTION_MESSAGE);
    }


}
