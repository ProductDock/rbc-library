package com.productdock.data.provider.out.postgresql;

import com.productdock.domain.Book;

import java.util.Calendar;

public class ReviewMother {
    private static final Long defaultBookId = 1L;
    private static final String defaultUserId = "::userId::";
    private static final String defaultUserFullName = "::userFullName::";
    private static final String defaultComment = "::comment::";
    private static final Short defaultRating = 2;
    private static final Integer defaultRecommendation = 3;

    public static Book.Review.ReviewBuilder defaultReviewBuilder() {
        var calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.APRIL, 5);

        return Book.Review.builder()
                .reviewCompositeKey(Book.Review.ReviewCompositeKey.builder()
                        .bookId(defaultBookId)
                        .userId(defaultUserId)
                        .build())
                .userFullName(defaultUserFullName)
                .comment(defaultComment)
                .rating(defaultRating)
                .recommendation(defaultRecommendation);
    }

    public static Book.Review defaultReview() {
        return defaultReviewBuilder().build();
    }
}
