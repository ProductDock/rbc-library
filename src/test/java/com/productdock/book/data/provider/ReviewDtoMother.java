package com.productdock.book.data.provider;

import com.productdock.book.Recommendation;
import com.productdock.book.ReviewDto;

import java.util.ArrayList;
import java.util.List;

public class ReviewDtoMother {

    private static final Long defaultBookId = null;
    private static final String defaultUserId = "::userId::";
    private static final String defaultUserFullName = "::userFullName::";
    private static final String defaultComment = "::comment::";
    private static final Short defaultRating = null;
    private static final List<Recommendation> defaultRecommendation = new ArrayList<>();

    public static ReviewDto.ReviewDtoBuilder defaultReviewDtoBuilder() {
        return ReviewDto.builder()
                .bookId(defaultBookId)
                .userId(defaultUserId)
                .userFullName(defaultUserFullName)
                .comment(defaultComment)
                .rating(defaultRating)
                .recommendation(defaultRecommendation);
    }

    public static ReviewDto defaultReviewDto() {
        return defaultReviewDtoBuilder().build();
    }

}
