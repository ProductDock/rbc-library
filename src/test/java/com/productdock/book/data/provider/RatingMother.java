package com.productdock.book.data.provider;

import com.productdock.book.Rating;

public class RatingMother {

    private static final Double defaultScore = 5.0;
    private static final Integer defaultCount = 1;

    public static Rating.RatingBuilder defaultRatingBuilder() {
        return Rating.builder()
                .score(defaultScore)
                .count(defaultCount);
    }

    public static Rating defaultRating() {
        return defaultRatingBuilder().build();
    }
}
