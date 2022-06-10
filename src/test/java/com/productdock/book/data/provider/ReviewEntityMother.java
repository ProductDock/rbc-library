package com.productdock.book.data.provider;

import com.productdock.book.ReviewEntity;

import java.util.Calendar;

public class ReviewEntityMother {

    private static final Long defaultBookId = 1L;
    private static final String defaultUserId = "::userId::";
    private static final String defaultUserFullName = "::userFullName::";
    private static final String defaultComment = "::comment::";
    private static final Short defaultRating = 2;
    private static final Integer defaultRecommendation = 3;

    public static ReviewEntity.ReviewEntityBuilder defaultReviewEntityBuilder() {
        var calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.APRIL, 5);

        return ReviewEntity.builder()
                .reviewCompositeKey(ReviewEntity.ReviewCompositeKey.builder()
                        .bookId(defaultBookId)
                        .userId(defaultUserId)
                        .build())
                .userFullName(defaultUserFullName)
                .comment(defaultComment)
                .rating(defaultRating)
                .date(calendar.getTime())
                .recommendation(defaultRecommendation);
    }

    public static ReviewEntity defaultReviewEntity() {
        return defaultReviewEntityBuilder().build();
    }

}
