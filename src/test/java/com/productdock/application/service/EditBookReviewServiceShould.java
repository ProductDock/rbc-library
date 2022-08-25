package com.productdock.application.service;

import com.productdock.application.port.out.persistence.ReviewPersistenceOutPort;
import com.productdock.domain.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EditBookReviewServiceShould {

    private static final Long BOOK_ID = 1L;

    @InjectMocks
    private EditBookReviewService service;

    @Mock
    private ReviewPersistenceOutPort reviewRepository;

    @Mock
    private PublishNewRatingService newRatingPublisher;

    @Test
    void editReviewWhenRatingNotEdited() {
        var reviewCompositeKey = Book.Review.ReviewCompositeKey.builder().bookId(BOOK_ID).build();
        var review = Book.Review.builder().rating((short) 5).reviewCompositeKey(reviewCompositeKey).build();
        var existingReview = Book.Review.builder().rating((short) 5).build();
        given(reviewRepository.findById(review.getReviewCompositeKey())).willReturn(Optional.of(existingReview));

        service.editReview(review);

        verify(reviewRepository).save(review);
    }

    @ParameterizedTest
    @MethodSource("testArguments")
    void editReviewWhenRatingEdited(Short existingRating, Short newRating) {
        var reviewCompositeKey = Book.Review.ReviewCompositeKey.builder().bookId(BOOK_ID).build();
        var review = Book.Review.builder().rating(newRating).reviewCompositeKey(reviewCompositeKey).build();
        var existingReview = Book.Review.builder().rating(existingRating).build();
        given(reviewRepository.findById(review.getReviewCompositeKey())).willReturn(Optional.of(existingReview));

        service.editReview(review);

        verify(reviewRepository).save(review);
        verify(newRatingPublisher).publishRating(BOOK_ID);
    }

    static Stream<Arguments> testArguments() {
        return Stream.of(
                Arguments.of((short) 5, (short) 3),
                Arguments.of(null, (short) 3),
                Arguments.of((short) 5, null)
        );
    }
}
