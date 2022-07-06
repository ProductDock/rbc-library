package com.productdock.application.service;

import com.productdock.adapter.out.kafka.BookRatingMessage;
import com.productdock.application.port.in.DeleteBookReviewUseCase;
import com.productdock.application.port.out.messaging.BookMessagingOutPort;
import com.productdock.application.port.out.persistence.BookPersistenceOutPort;
import com.productdock.application.port.out.persistence.ReviewPersistenceOutPort;
import com.productdock.domain.Book;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteBookReviewService implements DeleteBookReviewUseCase {

    private final ReviewPersistenceOutPort reviewRepository;
    private final BookPersistenceOutPort bookRepository;
    private final BookMessagingOutPort bookMessagingOutPort;
    @Value("${spring.kafka.topic.book-rating}")
    private String kafkaTopic;

    public void deleteReview(Long bookId, String userId) {
        var reviewCompositeKey = new Book.Review.ReviewCompositeKey(bookId, userId);
        var existingReview = reviewRepository.findById(reviewCompositeKey).orElseThrow();

        reviewRepository.deleteById(reviewCompositeKey);
        log.debug("Deleted a review: [{}]", existingReview);
        if (existingReview.getRating() == null) {
            return;
        }
        publishNewBookRating(bookId);
    }

    @SneakyThrows
    private void publishNewBookRating(Long bookId) {
        var book = bookRepository.findById(bookId).orElseThrow();
        bookMessagingOutPort.sendMessage(kafkaTopic, new BookRatingMessage(bookId, book.getRating().getScore(), book.getRating().getCount()));
    }
}
