package com.productdock.adapter.out.sql;

import com.productdock.adapter.out.sql.entity.ReviewJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<ReviewJpaEntity, ReviewJpaEntity.ReviewCompositeKey> {

}
