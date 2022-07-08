package com.productdock.adapter.out.postgresql;

import com.productdock.adapter.out.postgresql.entity.ReviewEntity;
import com.productdock.adapter.out.postgresql.mapper.ReviewCompositeKeyMapper;
import com.productdock.adapter.out.postgresql.mapper.ReviewMapper;
import com.productdock.domain.Book;
import com.productdock.exception.BookReviewException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ReviewRepositoryShould {

    private static final Book.Review.ReviewCompositeKey REVIEW_COMPOSITE_KEY = mock(Book.Review.ReviewCompositeKey.class);
    private static final ReviewEntity.ReviewCompositeKey REVIEW_COMPOSITE_KEY_ENTITY = mock(ReviewEntity.ReviewCompositeKey.class);
    private static final Optional<ReviewEntity> REVIEW_ENTITY = Optional.of(mock(ReviewEntity.class));
    private static final Book.Review REVIEW = mock(Book.Review.class);
    private static final String REVIEW_NOT_FOUND_EXCEPTION = "Review not found";

    @InjectMocks
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewJpaRepository reviewJpaRepository;

    @Mock
    private ReviewCompositeKeyMapper reviewCompositeKeyMapper;

    @Mock
    private ReviewMapper reviewMapper;

    @Test
    void findReviewWhenIdExist() {
        given(reviewCompositeKeyMapper.toEntity(REVIEW_COMPOSITE_KEY)).willReturn(REVIEW_COMPOSITE_KEY_ENTITY);
        given(reviewJpaRepository.findById(REVIEW_COMPOSITE_KEY_ENTITY)).willReturn(REVIEW_ENTITY);
        given(reviewMapper.toDomain(REVIEW_ENTITY.get())).willReturn(REVIEW);

        var review = reviewRepository.findById(REVIEW_COMPOSITE_KEY);

        assertThat(review).isPresent();
    }

    @Test
    void throwReviewNotFoundExceptionWhenNoIdExist() {
        given(reviewCompositeKeyMapper.toEntity(REVIEW_COMPOSITE_KEY)).willReturn(REVIEW_COMPOSITE_KEY_ENTITY);
        given(reviewJpaRepository.findById(REVIEW_COMPOSITE_KEY_ENTITY)).willReturn(Optional.empty());

        assertThatThrownBy(() -> reviewRepository.findById(REVIEW_COMPOSITE_KEY))
                .isInstanceOf(BookReviewException.class)
                .hasMessage(REVIEW_NOT_FOUND_EXCEPTION);
    }

}
