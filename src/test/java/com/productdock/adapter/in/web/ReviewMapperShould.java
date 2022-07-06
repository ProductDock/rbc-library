package com.productdock.adapter.in.web;

import com.productdock.adapter.in.web.mapper.ReviewDtoMapper;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.productdock.data.provider.provider.ReviewDtoMother.defaultReviewDto;
import static com.productdock.data.provider.provider.ReviewMother.defaultReview;
import static com.productdock.domain.Recommendation.JUNIOR;
import static com.productdock.domain.Recommendation.MEDIOR;

class ReviewMapperShould {

    private ReviewDtoMapper reviewMapper = Mappers.getMapper(ReviewDtoMapper.class);

    @Test
    void mapReviewDtoToReview() {

        var reviewDto = defaultReviewDto();

        var review = reviewMapper.toDomain(reviewDto);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(review.getReviewCompositeKey().getUserId()).isEqualTo(reviewDto.userId);
            softly.assertThat(review.getReviewCompositeKey().getBookId()).isEqualTo(reviewDto.bookId);
            softly.assertThat(review.getUserFullName()).isEqualTo(reviewDto.userFullName);
            softly.assertThat(review.getComment()).isEqualTo(reviewDto.comment);
            softly.assertThat(review.getRating()).isEqualTo(reviewDto.rating);
            softly.assertThat(review.getRecommendation()).isEqualTo(3);
        }
    }

    @Test
    void mapReviewToReviewDto() {

        var review = defaultReview();

        var reviewDto = reviewMapper.toDto(review);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(reviewDto.userId).isEqualTo(review.getReviewCompositeKey().getUserId());
            softly.assertThat(reviewDto.bookId).isEqualTo(review.getReviewCompositeKey().getBookId());
            softly.assertThat(reviewDto.userFullName).isEqualTo(review.getUserFullName());
            softly.assertThat(reviewDto.comment).isEqualTo(review.getComment());
            softly.assertThat(reviewDto.rating).isEqualTo(review.getRating());
            softly.assertThat(reviewDto.recommendation).containsExactlyInAnyOrder(JUNIOR, MEDIOR);
        }
    }

}
