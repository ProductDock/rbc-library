package com.productdock.application.port.out.persistence;

import com.productdock.domain.Book;

import java.util.Optional;

public interface ReviewPersistenceOutPort {

    boolean existsById(Book.Review.ReviewCompositeKey compositeKey);

    Optional<Book.Review> findById(Book.Review.ReviewCompositeKey compositeKey);

    void deleteById(Book.Review.ReviewCompositeKey compositeKey);

    void save(Book.Review review);
}
