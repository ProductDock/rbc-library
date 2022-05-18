package com.productdock.book;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
public class BookRatingCalculator {

    public Rating calculate(List<ReviewEntity> reviews) {
        var reviewsCount = (int) getRatedReviews(reviews).count();
        if (reviewsCount == 0) {
            return new Rating(0, reviewsCount);
        }
        double rating = getRatedReviews(reviews).mapToDouble(ReviewEntity::getRating).average().orElse(0.0);
        return new Rating(rating, reviewsCount);
    }

    private Stream<ReviewEntity> getRatedReviews(List<ReviewEntity> reviews) {
        return reviews.stream().filter(review -> review.getRating() != null);
    }
}
