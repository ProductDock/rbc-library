package com.productdock.application.port.in;

public interface DeleteBookReviewUseCase {

    void deleteReview(Long bookId, String userId);

}
