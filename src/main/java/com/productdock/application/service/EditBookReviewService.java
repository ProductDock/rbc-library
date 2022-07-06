package com.productdock.application.service;

import com.productdock.adapter.out.kafka.BookRatingMessage;
import com.productdock.application.port.in.EditBookReviewUseCase;
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
public class EditBookReviewService implements EditBookReviewUseCase {

    private final ReviewPersistenceOutPort reviewRepository;
    private final BookPersistenceOutPort bookRepository;
    private final BookMessagingOutPort bookOutPort;

    @Value("${spring.kafka.topic.book-rating}")
    private String kafkaTopic;

    public void editReview(Book.Review review) {
        var existingReview = reviewRepository.findById(review.getReviewCompositeKey()).orElseThrow();

        var existingRating = existingReview.getRating();
        reviewRepository.save(review);
        log.debug("Edited a review: [{}]", review);
        if (review.getRating().equals(existingRating)) {
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
