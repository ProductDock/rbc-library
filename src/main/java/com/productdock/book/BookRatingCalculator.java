package com.productdock.book;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookRatingCalculator {

    public Rating calculate(List<ReviewEntity> reviews) {
        var reviewsCount = (int) reviews.stream().filter(review -> review.getRating() != null).count();
        if(reviewsCount == 0){
            return new Rating(0, reviewsCount);
        }
        double ratingSum = reviews.stream().filter(review -> review.getRating() != null).map(r -> (double) r.getRating()).reduce(0.0, Double::sum);
        var rating = ratingSum / reviewsCount;
        return new Rating(rating, reviewsCount);
    }
}
