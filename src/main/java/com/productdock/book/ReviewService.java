package com.productdock.book;

import com.productdock.exception.BookReviewException;
import com.productdock.producer.JsonRecordPublisher;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
            throw new BookReviewException("The user cannot enter more than one comment for a particular book.");
        }
        reviewRepository.save(reviewEntity);
        if (reviewEntity.getRating() == null) {
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
