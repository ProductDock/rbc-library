package com.productdock.book;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static com.productdock.book.data.provider.ReviewEntityMother.defaultReviewEntity;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BookRatingCalculatorShould {

    @InjectMocks
    private BookRatingCalculator bookRatingCalculator;

    @Test
    void calculateBookRating() {
        var reviews = Arrays.asList(defaultReviewEntity(), defaultReviewEntity());

        var rating = bookRatingCalculator.calculate(reviews);

        assertThat(rating.getScore()).isEqualTo(2);
        assertThat(rating.getCount()).isEqualTo(2);
    }

    @Test
    void returnRatingNull_whenNoReviewsWithRating() {
        var reviews = List.of(ReviewEntity.builder().build());

        var rating = bookRatingCalculator.calculate(reviews);

        assertThat(rating.getScore()).isNull();
        assertThat(rating.getCount()).isZero();
    }

}
