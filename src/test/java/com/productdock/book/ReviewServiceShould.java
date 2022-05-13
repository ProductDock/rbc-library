package com.productdock.book;

import com.productdock.exception.BookReviewException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceShould {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewMapper reviewMapper;

    private static final ReviewDto reviewDtoMock = mock(ReviewDto.class);
    private static final ReviewEntity reviewEntityMock = mock(ReviewEntity.class);
    public static final String BOOK_REVIEW_EXCEPTION_MESSAGE = "The user cannot enter more than one comment for a particular book.";

    @Test
    void saveReview() {
        given(reviewMapper.toEntity(reviewDtoMock)).willReturn(reviewEntityMock);
        given(reviewRepository.existsById(reviewEntityMock.getReviewCompositeKey())).willReturn(false);

        reviewService.saveReview(reviewDtoMock);

        verify(reviewRepository).save(reviewEntityMock);
    }

    @Test
    void saveReview_whenUserAlreadyReviewedBook() {
        given(reviewMapper.toEntity(reviewDtoMock)).willReturn(reviewEntityMock);
        given(reviewRepository.existsById(reviewEntityMock.getReviewCompositeKey())).willReturn(true);

        assertThatThrownBy(() -> reviewService.saveReview(reviewDtoMock))
                .isInstanceOf(BookReviewException.class)
                .hasMessage(BOOK_REVIEW_EXCEPTION_MESSAGE);
    }
}
