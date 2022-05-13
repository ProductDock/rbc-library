package com.productdock.book;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<ReviewEntity, ReviewEntity.ReviewCompositeKey> {
}
