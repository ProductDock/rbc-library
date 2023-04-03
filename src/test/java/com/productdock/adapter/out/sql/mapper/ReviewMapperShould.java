package com.productdock.adapter.out.sql.mapper;

import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.productdock.data.provider.out.sql.ReviewJpaEntityMother.defaultReviewEntity;
import static com.productdock.data.provider.domain.ReviewMother.defaultReview;

class ReviewMapperShould {

    private ReviewMapper reviewMapper = Mappers.getMapper(ReviewMapper.class);

    @Test
    void mapReviewEntityToReview() {

        var reviewEntity = defaultReviewEntity();

        var review = reviewMapper.toDomain(reviewEntity);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(review.getReviewCompositeKey().getUserId()).isEqualTo(reviewEntity.getReviewCompositeKey().getUserId());
            softly.assertThat(review.getReviewCompositeKey().getBookId()).isEqualTo(reviewEntity.getReviewCompositeKey().getBookId());
            softly.assertThat(review.getUserFullName()).isEqualTo(reviewEntity.getUserFullName());
            softly.assertThat(review.getComment()).isEqualTo(reviewEntity.getComment());
            softly.assertThat(review.getRating()).isEqualTo(reviewEntity.getRating());
            softly.assertThat(review.getRecommendation()).isEqualTo(reviewEntity.getRecommendation());
        }
    }

    @Test
    void mapReviewToReviewEntity() {

        var review = defaultReview();

        var reviewEntity = reviewMapper.toEntity(review);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(reviewEntity.getReviewCompositeKey().getUserId()).isEqualTo(review.getReviewCompositeKey().getUserId());
            softly.assertThat(reviewEntity.getReviewCompositeKey().getBookId()).isEqualTo(review.getReviewCompositeKey().getBookId());
            softly.assertThat(reviewEntity.getUserFullName()).isEqualTo(review.getUserFullName());
            softly.assertThat(reviewEntity.getComment()).isEqualTo(review.getComment());
            softly.assertThat(reviewEntity.getRating()).isEqualTo(review.getRating());
            softly.assertThat(reviewEntity.getRecommendation()).isEqualTo(review.getRecommendation());
        }
    }
}
