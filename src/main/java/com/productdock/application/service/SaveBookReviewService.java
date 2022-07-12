package com.productdock.application.service;

import com.productdock.application.port.in.SaveBookReviewUseCase;
import com.productdock.application.port.out.persistence.ReviewPersistenceOutPort;
import com.productdock.domain.Book;
import com.productdock.domain.exception.BookReviewException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class SaveBookReviewService implements SaveBookReviewUseCase {

    private final ReviewPersistenceOutPort reviewRepository;
    private final PublishNewRatingService newRatingPublisher;

    @Override
    public void saveReview(Book.Review review) {
        if (reviewRepository.existsById(review.getReviewCompositeKey())) {
            log.warn("The User with id:{} is trying to add second review for book with id:{}",
                    review.getReviewCompositeKey().getUserId(), review.getReviewCompositeKey().getBookId());
            throw new BookReviewException("The user cannot enter more than one comment for a particular book.");
        }
        reviewRepository.save(review);
        log.debug("Saved a review: [{}]", review);
        if (review.getRating() == null) {
            return;
        }
        newRatingPublisher.publishRating(review.getReviewCompositeKey().getBookId());
    }

}
