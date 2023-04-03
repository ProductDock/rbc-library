package com.productdock.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.productdock.data.provider.domain.ReviewMother.defaultReview;
import static org.assertj.core.api.Assertions.assertThat;

class BookShould {

    private static final Book.Review EMPTY_REVIEW = Book.Review.builder().build();
    private static final Book.Review REVIEW = defaultReview();

    @Test
    void calculateRatingWhenNoReviewsWithRating() {
        var book = Book.builder().reviews(List.of(EMPTY_REVIEW)).build();

        Book.Rating rating = book.getRating();

        assertThat(rating.getScore()).isNull();
        assertThat(rating.getCount()).isZero();
    }

    @Test
    void calculateRatingWhenReviewsWithRatingExist() {
        var book = Book.builder().reviews(List.of(REVIEW, REVIEW, REVIEW)).build();

        Book.Rating rating = book.getRating();

        assertThat(rating.getScore()).isEqualTo(2);
        assertThat(rating.getCount()).isEqualTo(3);
    }
}
