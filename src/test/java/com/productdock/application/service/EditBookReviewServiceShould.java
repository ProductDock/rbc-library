package com.productdock.application.service;

import com.productdock.application.port.out.messaging.BookMessagingOutPort;
import com.productdock.application.port.out.persistence.BookPersistenceOutPort;
import com.productdock.application.port.out.persistence.ReviewPersistenceOutPort;
import com.productdock.domain.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EditBookReviewServiceShould {

    private static final Book.Rating RATING = mock(Book.Rating.class);
    private static final Optional<Book> BOOK = Optional.of(mock(Book.class));
    private static final Long BOOK_ID = 1L;
    @InjectMocks
    private EditBookReviewService service;

    @Mock
    private ReviewPersistenceOutPort reviewRepository;

    @Mock
    private BookPersistenceOutPort bookRepository;

    @Mock
    private BookMessagingOutPort bookOutPort;

    @Test
    void editReviewWhenRatingNotEdited(){
        var reviewCompositeKey = Book.Review.ReviewCompositeKey.builder().bookId(BOOK_ID).build();
        var review = Book.Review.builder().rating((short) 5).reviewCompositeKey(reviewCompositeKey).build();
        var existingReview = Book.Review.builder().rating((short) 5).build();
        given(reviewRepository.findById(review.getReviewCompositeKey())).willReturn(Optional.of(existingReview));

        service.editReview(review);

        verify(reviewRepository).save(review);
    }

   @Test
    void editReviewWhenRatingEdited(){
        var reviewCompositeKey = Book.Review.ReviewCompositeKey.builder().bookId(BOOK_ID).build();
        var review = Book.Review.builder().rating((short) 5).reviewCompositeKey(reviewCompositeKey).build();
        var existingReview = Book.Review.builder().rating((short) 3).build();
        given(reviewRepository.findById(review.getReviewCompositeKey())).willReturn(Optional.of(existingReview));
        given(bookRepository.findById(BOOK_ID)).willReturn(BOOK);
        given(BOOK.get().getRating()).willReturn(RATING);

        service.editReview(review);

        verify(reviewRepository).save(review);
    }
}
