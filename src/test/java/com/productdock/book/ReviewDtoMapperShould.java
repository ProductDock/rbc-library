package com.productdock.book;

import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.productdock.book.data.provider.ReviewEntityMother.defaultReviewEntity;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ReviewDtoMapperImpl.class})
class ReviewDtoMapperShould {

    @Autowired
    private ReviewDtoMapper reviewDtoMapper;

    @Test
    void mapReviewEntityToReviewDto() {

        var reviewEntity = defaultReviewEntity();
        var reviewDto = reviewDtoMapper.toDto(reviewEntity);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(reviewDto.comment).isEqualTo(reviewEntity.getComment());
            softly.assertThat(reviewDto.userFullName).isEqualTo(reviewEntity.getUserFullName());
            softly.assertThat(reviewDto.userId).isEqualTo(reviewEntity.getReviewCompositeKey().getUserId());
            softly.assertThat(reviewDto.rating).isEqualTo(reviewEntity.getRating());
            softly.assertThat(reviewDto.recommendation).containsExactlyInAnyOrder(Recommendation.MEDIOR, Recommendation.JUNIOR);
        }
    }
}
