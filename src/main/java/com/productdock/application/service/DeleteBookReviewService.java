package com.productdock.application.service;

import com.productdock.application.port.in.DeleteBookReviewUseCase;
import com.productdock.application.port.out.persistence.ReviewPersistenceOutPort;
import com.productdock.domain.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class DeleteBookReviewService implements DeleteBookReviewUseCase {

    private final ReviewPersistenceOutPort reviewRepository;
    private final PublishNewRatingService newRatingPublisher;

    @Override
    public void deleteReview(Long bookId, String userId) {
        var reviewCompositeKey = new Book.Review.ReviewCompositeKey(bookId, userId);
        var existingReview = reviewRepository.findById(reviewCompositeKey).orElseThrow();

        reviewRepository.deleteById(reviewCompositeKey);
        log.debug("Deleted a review: [{}]", existingReview);
        if (existingReview.getRating() == null) {
            return;
        }
        newRatingPublisher.publishRating(bookId);
    }

}
