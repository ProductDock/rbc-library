package com.productdock.book;

import com.productdock.exception.BookReviewException;
import com.productdock.producer.Publisher;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReviewService {

    private ReviewRepository reviewRepository;
    private ReviewMapper reviewMapper;
    private BookRepository bookRepository;
    private BookRatingCalculator calculator;
    private Publisher publisher;


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
        var book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            return;
        }
        var rating = calculator.calculate(book.get().getReviews());
        publisher.sendMessage(new BookRatingMessage(bookId, rating.getScore(), rating.getCount()));
    }

}
