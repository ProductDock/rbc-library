package com.productdock.book;

import java.util.List;

public class BookRatingCalculator {

    private BookRatingCalculator(){}

    public static Rating calculateBookRating(List<ReviewEntity> reviewDtos) {
        var reviewsCount = (int) reviewDtos.stream().filter(review -> review.getRating() != null).count();
        double rating = reviewDtos.stream().filter(review -> review.getRating() != null).map(r -> (double) r.getRating()).reduce(0.0, Double::sum);
        rating /= reviewsCount;
        return new Rating(rating, reviewsCount);
    }
}
