package com.productdock.application.service;

import com.productdock.adapter.out.kafka.BookRatingMessage;
import com.productdock.application.port.in.SaveBookReviewUseCase;
import com.productdock.application.port.out.messaging.BookMessagingOutPort;
import com.productdock.application.port.out.persistence.BookPersistenceOutPort;
import com.productdock.application.port.out.persistence.ReviewPersistenceOutPort;
import com.productdock.domain.Book;
import com.productdock.exception.BookReviewException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaveBookReviewService implements SaveBookReviewUseCase {

    private final ReviewPersistenceOutPort reviewRepository;
    private final BookPersistenceOutPort bookRepository;
    private final BookMessagingOutPort bookOutPort;

    @Value("${spring.kafka.topic.book-rating}")
    private String kafkaTopic;

    public void saveReview(Book.Review review) {
        if (reviewRepository.existsById(review.getReviewCompositeKey())) {
            log.warn("The User with id:{} is trying to add second review for book with id:{}",
                    review.getReviewCompositeKey().getUserId(), review.getReviewCompositeKey().getBookId());
            throw new BookReviewException("The user cannot enter more than one comment for a particular book.");
        }
        reviewRepository.save(review);
        log.debug("Saved a review: [{}]", review);
        if (review.getRating() == null) {
            return;
        }
        publishNewBookRating(review.getReviewCompositeKey().getBookId());
    }

    @SneakyThrows
    private void publishNewBookRating(Long bookId) {
        var book = bookRepository.findById(bookId).orElseThrow();
        bookOutPort.sendMessage(kafkaTopic, new BookRatingMessage(bookId, book.getRating().getScore(), book.getRating().getCount()));
    }
}
