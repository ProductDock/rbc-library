package com.productdock.adapter.in.web;

import com.productdock.application.port.in.DeleteBookReviewUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteBookReviewApiShould {


    @InjectMocks
    private DeleteBookReviewApi deleteBookReviewApi;

    @Mock
    private DeleteBookReviewUseCase deleteBookReviewUseCase;

    public static final long DEFAULT_BOOK_ID = 1;
    public static final String DEFAULT_USER_EMAIL = "test@productdock.com";
    public static final String WRONG_USER_ID_EXCEPTION_MESSAGE = "You don't have access for resource";

    @Test
    void deleteReviewForBook() {
        var authenticationMock = mock(Authentication.class);
        var jwtMock = mock(Jwt.class);

        given(authenticationMock.getCredentials()).willReturn(jwtMock);
        given(jwtMock.getClaim("email")).willReturn(DEFAULT_USER_EMAIL);

        deleteBookReviewApi.deleteReviewForBook(DEFAULT_BOOK_ID, DEFAULT_USER_EMAIL, authenticationMock);

        verify(deleteBookReviewUseCase).deleteReview(DEFAULT_BOOK_ID, DEFAULT_USER_EMAIL);
    }

    @Test
    void deleteReviewForBook_whenWrongUserId() {
        var authenticationMock = mock(Authentication.class);
        var jwtMock = mock(Jwt.class);

        given(authenticationMock.getCredentials()).willReturn(jwtMock);
        given(jwtMock.getClaim("email")).willReturn(DEFAULT_USER_EMAIL);

        assertThatThrownBy(() -> deleteBookReviewApi.deleteReviewForBook(DEFAULT_BOOK_ID, "::wrongId::", authenticationMock))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage(WRONG_USER_ID_EXCEPTION_MESSAGE);
    }
}
