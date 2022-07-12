package com.productdock.application.service;

import com.productdock.application.port.in.EditBookReviewUseCase;
import com.productdock.application.port.in.PublishNewRatingUseCase;
import com.productdock.application.port.out.persistence.ReviewPersistenceOutPort;
import com.productdock.domain.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EditBookReviewService implements EditBookReviewUseCase {

    private final ReviewPersistenceOutPort reviewRepository;
    private final PublishNewRatingUseCase newRatingPublisher;

    @Override
    public void editReview(Book.Review review) {
        var existingReview = reviewRepository.findById(review.getReviewCompositeKey()).orElseThrow();

        reviewRepository.save(review);
        log.debug("Edited a review: [{}]", review);

        var existingRating = existingReview.getRating();
        if (review.getRating().equals(existingRating)) {
            return;
        }
        newRatingPublisher.publishRating(review.getReviewCompositeKey().getBookId());
    }

}
