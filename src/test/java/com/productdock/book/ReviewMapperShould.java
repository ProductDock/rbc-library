package com.productdock.book;

import com.productdock.book.data.provider.ReviewDtoMother;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.productdock.book.Recommendation.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ReviewMapperImpl.class})
class ReviewMapperShould {

    @Autowired
    ReviewMapper reviewMapper;

    @Test
    void convertReviewEntityToDto() {

        var reviewEntity = ReviewEntity.builder()
                .reviewCompositeKey(new ReviewEntity.ReviewCompositeKey(1L, "::userId::"))
                .userFullName("::userFullName::")
                .comment("::comment::")
                .rating((short) 5)
                .recommendation(7)
                .build();

        var reviewDto = reviewMapper.toDto(reviewEntity);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(reviewDto.userId).isEqualTo(reviewEntity.getReviewCompositeKey().getUserId());
            softly.assertThat(reviewDto.bookId).isEqualTo(reviewEntity.getReviewCompositeKey().getBookId());
            softly.assertThat(reviewDto.userFullName).isEqualTo(reviewEntity.getUserFullName());
            softly.assertThat(reviewDto.comment).isEqualTo(reviewEntity.getComment());
            softly.assertThat(reviewDto.rating).isEqualTo(reviewEntity.getRating());
            softly.assertThat(reviewDto.recommendation).containsExactlyInAnyOrder(SENIOR, JUNIOR, MEDIOR);
        }
    }

    @Test
    void convertReviewDtoToEntity() {

        var reviewDto = ReviewDtoMother.defaultReviewDtoBuilder()
                .bookId(1L)
                .recommendation(List.of(MEDIOR, SENIOR))
                .build();

        var reviewEntity = reviewMapper.toEntity(reviewDto);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(reviewEntity.getReviewCompositeKey().getUserId()).isEqualTo(reviewDto.userId);
            softly.assertThat(reviewEntity.getReviewCompositeKey().getBookId()).isEqualTo(reviewDto.bookId);
            softly.assertThat(reviewEntity.getComment()).isEqualTo(reviewDto.comment);
            softly.assertThat(reviewEntity.getRating()).isEqualTo(reviewDto.rating);
            softly.assertThat(reviewEntity.getRecommendation()).isEqualTo(6);
            softly.assertThat(reviewEntity.getUserFullName()).isEqualTo(reviewDto.userFullName);
        }
    }

}
