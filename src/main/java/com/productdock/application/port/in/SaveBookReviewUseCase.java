package com.productdock.application.port.in;

import com.productdock.domain.Book;

public interface SaveBookReviewUseCase {

    void saveReview(Book.Review review);

}
