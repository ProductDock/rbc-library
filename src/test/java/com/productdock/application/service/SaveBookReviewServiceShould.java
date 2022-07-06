package com.productdock.application.service;

import com.productdock.application.port.out.messaging.BookMessagingOutPort;
import com.productdock.application.port.out.persistence.BookPersistenceOutPort;
import com.productdock.application.port.out.persistence.ReviewPersistenceOutPort;
import com.productdock.domain.Book;
import com.productdock.exception.BookReviewException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SaveBookReviewServiceShould {

    private static final Book.Review.ReviewCompositeKey REVIEW_COMPOSITE_KEY = mock(Book.Review.ReviewCompositeKey.class);
    private static final Book.Review REVIEW = mock(Book.Review.class);
    private static final Book.Rating RATING = mock(Book.Rating.class);
    private static final Optional<Book> BOOK = Optional.of(mock(Book.class));
    private static final Long BOOK_ID = 1L;
    private static final String USER_LEFT_REVIEW_FOR_BOOK = "The user cannot enter more than one comment for a particular book.";

    @InjectMocks
    private SaveBookReviewService service;

    @Mock
    private ReviewPersistenceOutPort reviewRepository;

    @Mock
    private BookPersistenceOutPort bookRepository;

    @Mock
    private BookMessagingOutPort bookOutPort;

    @Test
    void saveReview(){
        given(REVIEW.getReviewCompositeKey()).willReturn(REVIEW_COMPOSITE_KEY);
        given(reviewRepository.existsById(REVIEW_COMPOSITE_KEY)).willReturn(false);
        given(REVIEW_COMPOSITE_KEY.getBookId()).willReturn(BOOK_ID);
        given(bookRepository.findById(BOOK_ID)).willReturn(BOOK);
        given(BOOK.get().getRating()).willReturn(RATING);

        service.saveReview(REVIEW);

        verify(reviewRepository).save(REVIEW);
    }

    @Test
    void throwExceptionWhenReviewWithCompositeKeyExist(){
        given(REVIEW.getReviewCompositeKey()).willReturn(REVIEW_COMPOSITE_KEY);
        given(reviewRepository.existsById(REVIEW_COMPOSITE_KEY)).willReturn(true);

        assertThatThrownBy(() -> service.saveReview(REVIEW))
                .isInstanceOf(BookReviewException.class)
                .hasMessage(USER_LEFT_REVIEW_FOR_BOOK);
    }

}
