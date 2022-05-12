package com.productdock.book;

import com.productdock.exception.BadRequestException;
import org.springframework.stereotype.Service;

@Service
public record ReviewService(ReviewRepository reviewRepository,
                            ReviewMapper reviewMapper) {

    public void saveReview(ReviewDto reviewDto) {
        var reviewEntity = reviewMapper.toEntity(reviewDto);

        if (reviewRepository.existsById(reviewEntity.getReviewCompositeKey())) {
            throw new BadRequestException("The user cannot enter more than one comment for a particular book.");
        }
        reviewRepository.save(reviewEntity);
    }

}
