package com.productdock.book;

import com.productdock.exception.BookReviewException;
import com.productdock.producer.JsonRecordPublisher;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReviewService {

    private ReviewRepository reviewRepository;
    private ReviewMapper reviewMapper;
    private BookRatingCalculator calculator;
    private JsonRecordPublisher jsonRecordPublisher;


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
        jsonRecordPublisher.sendMessage(new BookRatingMessage(bookId, rating.getScore(), rating.getCount()));
    }

}
