package com.productdock.book;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static com.productdock.book.data.provider.ReviewEntityMother.defaultReviewEntity;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BookRatingCalculatorShould {

    @InjectMocks
    private BookRatingCalculator bookRatingCalculator;

    @Test
    void calculateBookRating(){
        var reviews = Arrays.asList(defaultReviewEntity(), defaultReviewEntity());

        var rating = bookRatingCalculator.calculate(reviews);

        assertThat(rating.getScore()).isEqualTo(2);
        assertThat(rating.getCount()).isEqualTo(2);
    }

}
