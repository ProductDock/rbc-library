package com.productdock.adapter.out.sql;

import com.productdock.adapter.out.sql.entity.ReviewJpaEntity;
import com.productdock.adapter.out.sql.mapper.ReviewCompositeKeyMapper;
import com.productdock.adapter.out.sql.mapper.ReviewMapper;
import com.productdock.domain.Book;
import com.productdock.domain.exception.BookReviewException;
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
class ReviewPersistenceAdapterShould {

    private static final Book.Review.ReviewCompositeKey REVIEW_COMPOSITE_KEY = mock(Book.Review.ReviewCompositeKey.class);
    private static final ReviewJpaEntity.ReviewCompositeKey REVIEW_COMPOSITE_KEY_ENTITY = mock(ReviewJpaEntity.ReviewCompositeKey.class);
    private static final Optional<ReviewJpaEntity> REVIEW_ENTITY = Optional.of(mock(ReviewJpaEntity.class));
    private static final Book.Review REVIEW = mock(Book.Review.class);
    private static final String REVIEW_NOT_FOUND_EXCEPTION = "Review not found";

    @InjectMocks
    private ReviewPersistenceAdapter reviewPersistenceAdapter;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewCompositeKeyMapper reviewCompositeKeyMapper;

    @Mock
    private ReviewMapper reviewMapper;

    @Test
    void findReviewWhenIdExist() {
        given(reviewCompositeKeyMapper.toEntity(REVIEW_COMPOSITE_KEY)).willReturn(REVIEW_COMPOSITE_KEY_ENTITY);
        given(reviewRepository.findById(REVIEW_COMPOSITE_KEY_ENTITY)).willReturn(REVIEW_ENTITY);
        given(reviewMapper.toDomain(REVIEW_ENTITY.get())).willReturn(REVIEW);

        var review = reviewPersistenceAdapter.findById(REVIEW_COMPOSITE_KEY);

        assertThat(review).isPresent();
    }

    @Test
    void throwReviewNotFoundExceptionWhenNoIdExist() {
        given(reviewCompositeKeyMapper.toEntity(REVIEW_COMPOSITE_KEY)).willReturn(REVIEW_COMPOSITE_KEY_ENTITY);
        given(reviewRepository.findById(REVIEW_COMPOSITE_KEY_ENTITY)).willReturn(Optional.empty());

        assertThatThrownBy(() -> reviewPersistenceAdapter.findById(REVIEW_COMPOSITE_KEY))
                .isInstanceOf(BookReviewException.class)
                .hasMessage(REVIEW_NOT_FOUND_EXCEPTION);
    }

}
