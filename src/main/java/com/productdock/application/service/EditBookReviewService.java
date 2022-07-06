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

    public void editReview(Book.Review review) {
        var existingReview = reviewRepository.findById(review.getReviewCompositeKey()).orElseThrow();

        var existingRating = existingReview.getRating();
        reviewRepository.save(review);
        log.debug("Edited a review: [{}]", review);
        if (review.getRating().equals(existingRating)) {
            return;
        }
        newRatingPublisher.publishRating(review.getReviewCompositeKey().getBookId());
    }

}
