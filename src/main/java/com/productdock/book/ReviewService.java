package com.productdock.book;

import org.springframework.stereotype.Service;

@Service
public record ReviewService(ReviewRepository reviewRepository,
                            ReviewMapper reviewMapper) {

    public ReviewDto saveReview(ReviewDto reviewDto) {
        var reviewEntity = reviewRepository.save(reviewMapper.toEntity(reviewDto));
        return reviewMapper.toDto(reviewEntity);
    }

}
