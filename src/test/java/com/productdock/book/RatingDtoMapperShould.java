package com.productdock.book;

import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.productdock.book.data.provider.RatingMother.defaultRating;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RatingDtoMapperImpl.class})
class RatingDtoMapperShould {

    @Autowired
    private RatingDtoMapper ratingDtoMapper;

    @Test
    void mapReviewEntityToReviewDto() {
        var rating = defaultRating();

        var ratingDto = ratingDtoMapper.toDto(rating);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(ratingDto.score).isEqualTo(rating.getScore());
            softly.assertThat(ratingDto.count).isEqualTo(rating.getCount());

        }
    }
}
