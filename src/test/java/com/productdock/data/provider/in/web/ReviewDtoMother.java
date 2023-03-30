package com.productdock.data.provider.in.web;

import com.productdock.adapter.in.web.dto.ReviewDto;
import com.productdock.adapter.in.web.mapper.Recommendation;

import java.util.List;

public class ReviewDtoMother {

    private static final Long defaultBookId = 1L;
    private static final String defaultUserId = "::userId::";
    private static final String defaultUserFullName = "::userFullName::";
    private static final String defaultComment = "::comment::";
    private static final Short defaultRating = 5;
    private static final List<Recommendation> defaultRecommendation = List.of(Recommendation.JUNIOR, Recommendation.MEDIOR);

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
