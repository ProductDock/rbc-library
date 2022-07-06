package com.productdock.adapter.out.postresql;

import com.productdock.adapter.out.postresql.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewJpaRepository extends JpaRepository<ReviewEntity, ReviewEntity.ReviewCompositeKey> {

    @Query("select r from ReviewEntity r where r.reviewCompositeKey.bookId = ?1")
    List<ReviewEntity> findByBookId(Long bookId);
}
