package com.productdock.adapter.in.web;

import com.productdock.adapter.in.web.mapper.ReviewDtoMapper;
import com.productdock.application.port.in.SaveBookReviewUseCase;
import com.productdock.domain.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import static com.productdock.data.provider.in.web.ReviewDtoMother.defaultReviewDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateBookReviewApiShould {

    @InjectMocks
    private CreateBookReviewApi createBookReviewApi;

    @Mock
    private SaveBookReviewUseCase saveBookReviewUseCase;

    @Mock
    private ReviewDtoMapper reviewMapper;

    public static final long DEFAULT_BOOK_ID = 1;
    public static final String DEFAULT_USER_EMAIL = "test@productdock.com";
    public static final String DEFAULT_USER_FULL_NAME = "test name";

    @Test
    void createReviewForBook() {
        var reviewDto = defaultReviewDto();
        var authenticationMock = mock(Authentication.class);
        var jwtMock = mock(Jwt.class);
        var review = mock(Book.Review.class);

        given(authenticationMock.getCredentials()).willReturn(jwtMock);
        given(jwtMock.getClaim("email")).willReturn(DEFAULT_USER_EMAIL);
        given(jwtMock.getClaim("fullName")).willReturn(DEFAULT_USER_FULL_NAME);
        given(reviewMapper.toDomain(reviewDto)).willReturn(review);

        createBookReviewApi.createReviewForBook(DEFAULT_BOOK_ID, reviewDto, authenticationMock);

        verify(saveBookReviewUseCase).saveReview(review);
        assertThat(reviewDto.bookId).isEqualTo(DEFAULT_BOOK_ID);
        assertThat(reviewDto.userId).isEqualTo(DEFAULT_USER_EMAIL);
        assertThat(reviewDto.userFullName).isEqualTo(DEFAULT_USER_FULL_NAME);
    }
}
