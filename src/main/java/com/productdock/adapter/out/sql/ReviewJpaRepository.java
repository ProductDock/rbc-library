package com.productdock.adapter.out.sql;

import com.productdock.adapter.out.sql.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewJpaRepository extends JpaRepository<ReviewEntity, ReviewEntity.ReviewCompositeKey> {

}
