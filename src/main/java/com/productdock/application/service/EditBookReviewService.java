package com.productdock.application.service;

import com.productdock.application.port.in.EditBookReviewUseCase;
import com.productdock.application.port.out.persistence.ReviewPersistenceOutPort;
import com.productdock.domain.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class EditBookReviewService implements EditBookReviewUseCase {

    private final ReviewPersistenceOutPort reviewRepository;
    private final PublishNewRatingService newRatingPublisher;

    @Override
    public void editReview(Book.Review review) {
        var existingReview = reviewRepository.findById(review.getReviewCompositeKey()).orElseThrow();

        reviewRepository.save(review);
        log.debug("Edited a review: [{}]", review);

        var existingRating = existingReview.getRating();
        if (ratingNotChanged(review.getRating(), existingRating)) {
            return;
        }
        newRatingPublisher.publishRating(review.getReviewCompositeKey().getBookId());
    }

    private boolean ratingNotChanged(Short newRating, Short existingRating) {
        if (existingRating != null) {
            return existingRating.equals(newRating);
        }
        return newRating == null;
    }
}
