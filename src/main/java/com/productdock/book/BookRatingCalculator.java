package com.productdock.book;

import java.util.List;

public class BookRatingCalculator {

    private BookRatingCalculator(){}

    public static BookDto.RatingDto calculateBookRating(List<BookDto.ReviewDto> reviewDtos) {
        var reviews = reviewDtos.stream().filter(review -> review.rating != null);
        double rating = reviews.map(r -> (double) r.rating).reduce(0.0, Double::sum);
        rating /= reviews.count();
        return new BookDto.RatingDto(rating, (int) reviews.count());
    }
}
