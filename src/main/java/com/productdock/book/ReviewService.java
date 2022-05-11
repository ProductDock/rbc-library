package com.productdock.book;

import com.productdock.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

@Service
public record ReviewService(ReviewRepository reviewRepository,
                            ReviewMapper reviewMapper) {

    public ReviewDto saveReview(ReviewDto reviewDto) {
        var reviewEntity = reviewMapper.toEntity(reviewDto);

        if (reviewRepository.existsById(reviewEntity.getReviewCompositeKey())) {
            throw new BadRequestException("The user cannot enter more than one comment for a particular book.");
        }

        try {
            var newReviewEntity = reviewRepository.save(reviewEntity);
            return reviewMapper.toDto(newReviewEntity);
        } catch (TransactionSystemException e) {
            String message = "The content of the review is not valid. A comment cannot be longer than 500 characters, and the rating must be between 1 and 5.";
            throw new BadRequestException(message);
        }
    }

}
