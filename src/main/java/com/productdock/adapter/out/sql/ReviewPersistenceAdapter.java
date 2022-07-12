package com.productdock.adapter.out.sql;

import com.productdock.adapter.out.sql.mapper.ReviewCompositeKeyMapper;
import com.productdock.adapter.out.sql.mapper.ReviewMapper;
import com.productdock.application.port.out.persistence.ReviewPersistenceOutPort;
import com.productdock.domain.Book;
import com.productdock.domain.exception.BookReviewException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Slf4j
@AllArgsConstructor
class ReviewPersistenceAdapter implements ReviewPersistenceOutPort {

    private ReviewRepository reviewRepository;
    private ReviewCompositeKeyMapper reviewCompositeKeyMapper;
    private ReviewMapper reviewMapper;

    @Override
    public boolean existsById(Book.Review.ReviewCompositeKey compositeKey) {
        var compositeKeyEntity = reviewCompositeKeyMapper.toEntity(compositeKey);
        return reviewRepository.existsById(compositeKeyEntity);
    }

    @Override
    public Optional<Book.Review> findById(Book.Review.ReviewCompositeKey compositeKey) {
        var compositeKeyEntity = reviewCompositeKeyMapper.toEntity(compositeKey);
        var review = reviewRepository.findById(compositeKeyEntity).map(reviewJpaEntity -> reviewMapper.toDomain(reviewJpaEntity));
        if (review.isEmpty()) {
            log.debug("Unable to find a review with composite key: {}", compositeKey);
            throw new BookReviewException("Review not found");
        }
        return review;
    }

    @Override
    public void deleteById(Book.Review.ReviewCompositeKey compositeKey) {
        var compositeKeyEntity = reviewCompositeKeyMapper.toEntity(compositeKey);
        reviewRepository.deleteById(compositeKeyEntity);
    }

    @Override
    public void save(Book.Review review) {
        var reviewEntity = reviewMapper.toEntity(review);
        reviewRepository.save(reviewEntity);
    }
}
