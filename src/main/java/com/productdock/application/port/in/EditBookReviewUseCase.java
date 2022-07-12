package com.productdock.application.port.in;

import com.productdock.domain.Book;

public interface EditBookReviewUseCase {

    void editReview(Book.Review review);

}
