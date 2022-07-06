package com.productdock.adapter.out.postresql.mapper;

import com.productdock.domain.Book;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class ReviewCompositeKeyMapperShould {

    private ReviewCompositeKeyMapper reviewCompositeKeyMapper = Mappers.getMapper(ReviewCompositeKeyMapper.class);
    private static final Long BOOK_ID = 1L;
    private static final String USER_ID = "::user::";

    @Test
    void mapReviewEntityToReview() {

        var compositeKey = Book.Review.ReviewCompositeKey.builder().bookId(BOOK_ID).userId(USER_ID).build();

        var compositeKeyEntity = reviewCompositeKeyMapper.toEntity(compositeKey);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(compositeKeyEntity.getUserId()).isEqualTo(compositeKey.getUserId());
            softly.assertThat(compositeKeyEntity.getBookId()).isEqualTo(compositeKey.getBookId());
        }
    }


}
