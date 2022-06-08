package com.productdock.book;

import com.productdock.exception.BookReviewException;
import com.productdock.producer.JsonRecordPublisher;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final BookRatingCalculator calculator;
    private final JsonRecordPublisher jsonRecordPublisher;

    @Value("${spring.kafka.topic.book-rating}")
    private String kafkaTopic;

    public void saveReview(ReviewDto reviewDto) {
        var reviewEntity = reviewMapper.toEntity(reviewDto);

        if (reviewRepository.existsById(reviewEntity.getReviewCompositeKey())) {
            log.warn("The User with id:{} is trying to add second review for book with id:{}",
                    reviewDto.userId, reviewDto.bookId);
            throw new BookReviewException("The user cannot enter more than one comment for a particular book.");
        }
        reviewRepository.save(reviewEntity);
        log.debug("Saved a review: [{}]", reviewEntity);
        if (reviewEntity.getRating() == null) {
            return;
        }
        publishNewBookRating(reviewDto.bookId);
    }

    public void editReview(ReviewDto reviewDto) {
        var reviewEntity = reviewMapper.toEntity(reviewDto);
        var existingReview = reviewRepository.findById(reviewEntity.getReviewCompositeKey());

        if (existingReview.isEmpty()) {
            log.warn("The User with id:{} is trying to edit not existing review with id:[{},{}]",
                    reviewDto.userId, reviewDto.bookId, reviewDto.userId);
            throw new BookReviewException("Review not found");
        }
        var existingRating = existingReview.get().getRating();

        reviewRepository.save(reviewEntity);
        log.debug("Edited a review: [{}]", reviewEntity);
        if (reviewEntity.getRating().equals(existingRating)) {
            return;
        }
        publishNewBookRating(reviewDto.bookId);
    }

    @SneakyThrows
    private void publishNewBookRating(Long bookId) {
        var reviews = reviewRepository.findByBookId(bookId);
        var rating = calculator.calculate(reviews);
        jsonRecordPublisher.sendMessage(kafkaTopic, new BookRatingMessage(bookId, rating.getScore(), rating.getCount()));
    }

}
