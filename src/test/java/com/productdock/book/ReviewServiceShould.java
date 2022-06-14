package com.productdock.book;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.productdock.exception.BookReviewException;
import com.productdock.producer.JsonRecordPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceShould {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private BookRatingCalculator calculator;

    @Mock
    private JsonRecordPublisher jsonRecordPublisher;

    @Mock
    private ReviewMapper reviewMapper;

    private static final ReviewDto reviewDtoMock = mock(ReviewDto.class);
    private static final ReviewEntity reviewEntityMock = mock(ReviewEntity.class);
    private static final Optional<ReviewEntity> existingReviewEntityMock = Optional.of(mock(ReviewEntity.class));
    private static final ReviewEntity.ReviewCompositeKey reviewCompositeKey = mock(ReviewEntity.ReviewCompositeKey.class);
    private static final List reviewListMock = mock(List.class);
    private static final Rating ratingMock = mock(Rating.class);
    private static final String SAVE_BOOK_REVIEW_EXCEPTION_MESSAGE = "The user cannot enter more than one comment for a particular book.";
    private static final String MISSING_BOOK_REVIEW_EXCEPTION_MESSAGE = "Review not found";

    @Captor
    private ArgumentCaptor<BookRatingMessage> bookRatingMessageCaptor;

    @Test
    void saveReview() throws Exception {
        given(reviewMapper.toEntity(reviewDtoMock)).willReturn(reviewEntityMock);
        given(reviewRepository.existsById(reviewEntityMock.getReviewCompositeKey())).willReturn(false);
        given(reviewRepository.findByBookId(reviewDtoMock.bookId)).willReturn(reviewListMock);
        given(reviewEntityMock.getRating()).willReturn((short) 1);
        given(calculator.calculate(reviewListMock)).willReturn(ratingMock);

        reviewService.saveReview(reviewDtoMock);

        verify(reviewRepository).save(reviewEntityMock);
        assertThatValidMessagesPublishedToKafka();
    }

    @Test
    void editReview() throws Exception {
        given(reviewMapper.toEntity(reviewDtoMock)).willReturn(reviewEntityMock);
        given(reviewRepository.findById(reviewEntityMock.getReviewCompositeKey())).willReturn(existingReviewEntityMock);
        given(reviewRepository.findByBookId(reviewDtoMock.bookId)).willReturn(reviewListMock);
        given(reviewEntityMock.getRating()).willReturn((short) 1);
        given(existingReviewEntityMock.get().getRating()).willReturn((short) 2);
        given(calculator.calculate(reviewListMock)).willReturn(ratingMock);

        reviewService.editReview(reviewDtoMock);

        verify(reviewRepository).save(reviewEntityMock);
        assertThatValidMessagesPublishedToKafka();
    }

    @Test
    void deleteReview() {
        var reviewCompositeKey = new ReviewEntity.ReviewCompositeKey(1L, "::userId::");
        given(reviewRepository.findById(reviewCompositeKey)).willReturn(existingReviewEntityMock);
        given(reviewRepository.findByBookId(1L)).willReturn(reviewListMock);
        given(existingReviewEntityMock.get().getRating()).willReturn((short) 2);
        given(reviewEntityMock.getRating()).willReturn((short) 1);
        given(calculator.calculate(reviewListMock)).willReturn(ratingMock);

        reviewService.deleteReview(1L, "::userId::");

        verify(reviewRepository).deleteById(reviewCompositeKey);
    }

    @Test
    void deleteReview_whenReviewNotExist() throws Exception {
        var reviewCompositeKey = new ReviewEntity.ReviewCompositeKey(1L, "::userId::");
        given(reviewRepository.findById(reviewCompositeKey)).willReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.deleteReview(1L, "::userId::"))
                .isInstanceOf(BookReviewException.class)
                .hasMessage(MISSING_BOOK_REVIEW_EXCEPTION_MESSAGE);

    }

    private void assertThatValidMessagesPublishedToKafka() throws ExecutionException, InterruptedException, JsonProcessingException {
        verify(jsonRecordPublisher).sendMessage(any(), bookRatingMessageCaptor.capture());

        var bookRatingMessageValue = bookRatingMessageCaptor.getValue();
        assertThat(bookRatingMessageValue.getBookId()).isEqualTo(reviewDtoMock.bookId);
        assertThat(bookRatingMessageValue.getRating()).isEqualTo(ratingMock.getScore());
        assertThat(bookRatingMessageValue.getRatingsCount()).isEqualTo(ratingMock.getCount());
    }

    @Test
    void saveReview_whenRatingMissing() throws Exception {
        given(reviewMapper.toEntity(reviewDtoMock)).willReturn(reviewEntityMock);
        given(reviewEntityMock.getRating()).willReturn(null);
        given(reviewRepository.existsById(reviewEntityMock.getReviewCompositeKey())).willReturn(false);

        reviewService.saveReview(reviewDtoMock);

        verify(reviewRepository).save(reviewEntityMock);
        verify(jsonRecordPublisher, times(0)).sendMessage(eq("book-rating"), any());
    }

    @Test
    void saveReview_whenUserAlreadyReviewedBook() {
        given(reviewMapper.toEntity(reviewDtoMock)).willReturn(reviewEntityMock);
        given(reviewRepository.existsById(reviewEntityMock.getReviewCompositeKey())).willReturn(true);

        assertThatThrownBy(() -> reviewService.saveReview(reviewDtoMock))
                .isInstanceOf(BookReviewException.class)
                .hasMessage(SAVE_BOOK_REVIEW_EXCEPTION_MESSAGE);
    }


    @Test
    void editReview_whenRatingNotChanged() throws Exception {
        given(reviewMapper.toEntity(reviewDtoMock)).willReturn(reviewEntityMock);
        given(reviewRepository.findById(reviewEntityMock.getReviewCompositeKey())).willReturn(existingReviewEntityMock);
        given(reviewEntityMock.getRating()).willReturn((short) 1);
        given(existingReviewEntityMock.get().getRating()).willReturn((short) 1);

        reviewService.editReview(reviewDtoMock);

        verify(reviewRepository).save(reviewEntityMock);
        verify(jsonRecordPublisher, times(0)).sendMessage(eq("book-rating"), any());
    }

    @Test
    void editReview_whenReviewNotExist() throws Exception {
        given(reviewMapper.toEntity(reviewDtoMock)).willReturn(reviewEntityMock);
        given(reviewRepository.findById(reviewEntityMock.getReviewCompositeKey())).willReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.editReview(reviewDtoMock))
                .isInstanceOf(BookReviewException.class)
                .hasMessage(MISSING_BOOK_REVIEW_EXCEPTION_MESSAGE);
    }

}
