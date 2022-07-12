package com.productdock.application.service;

import com.productdock.application.port.out.persistence.ReviewPersistenceOutPort;
import com.productdock.domain.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteBookReviewServiceShould {

    private static final Optional<Book.Review> REVIEW = Optional.of(mock(Book.Review.class));
    private static final Long BOOK_ID = 1L;
    private static final String USER_ID = "::userId::";
    private static final short REVIEW_RATING = 1;

    @InjectMocks
    private DeleteBookReviewService service;

    @Mock
    private ReviewPersistenceOutPort reviewRepository;

    @Mock
    private PublishNewRatingService newRatingPublisher;

    @Test
    void deleteReviewWhenReviewHasNoRating() {
        var key = Book.Review.ReviewCompositeKey.builder().bookId(BOOK_ID).userId(USER_ID).build();
        given(reviewRepository.findById(key)).willReturn(REVIEW);
        given(REVIEW.get().getRating()).willReturn(null);

        service.deleteReview(BOOK_ID, USER_ID);

        verify(reviewRepository).deleteById(key);
    }

    @Test
    void deleteReviewAndPublishMessageWhenReviewHasRating() {
        var key = Book.Review.ReviewCompositeKey.builder().bookId(BOOK_ID).userId(USER_ID).build();
        given(reviewRepository.findById(key)).willReturn(REVIEW);
        given(REVIEW.get().getRating()).willReturn(REVIEW_RATING);

        service.deleteReview(BOOK_ID, USER_ID);

        verify(reviewRepository).deleteById(key);
        verify(newRatingPublisher).publishRating(BOOK_ID);
    }
}
