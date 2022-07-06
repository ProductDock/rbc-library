package com.productdock.application.service;

import com.productdock.application.port.in.PublishNewRatingUseCase;
import com.productdock.application.port.out.persistence.ReviewPersistenceOutPort;
import com.productdock.domain.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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
    private PublishNewRatingUseCase newRatingPublisher;

    @Test
    void editReviewWhenRatingNotEdited(){
        var reviewCompositeKey = Book.Review.ReviewCompositeKey.builder().bookId(BOOK_ID).build();
        var review = Book.Review.builder().rating((short) 5).reviewCompositeKey(reviewCompositeKey).build();
        var existingReview = Book.Review.builder().rating((short) 5).build();
        given(reviewRepository.findById(review.getReviewCompositeKey())).willReturn(Optional.of(existingReview));

        service.editReview(review);

        verify(reviewRepository).save(review);
    }

   @Test
    void editReviewWhenRatingEdited(){
        var reviewCompositeKey = Book.Review.ReviewCompositeKey.builder().bookId(BOOK_ID).build();
        var review = Book.Review.builder().rating((short) 5).reviewCompositeKey(reviewCompositeKey).build();
        var existingReview = Book.Review.builder().rating((short) 3).build();
        given(reviewRepository.findById(review.getReviewCompositeKey())).willReturn(Optional.of(existingReview));

        service.editReview(review);

        verify(reviewRepository).save(review);
    }
}
