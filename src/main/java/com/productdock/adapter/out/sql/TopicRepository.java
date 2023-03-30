package com.productdock.adapter.out.sql;

import com.productdock.adapter.out.sql.entity.TopicJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface TopicRepository extends JpaRepository<TopicJpaEntity, Long> {

    @Query("select t from TopicJpaEntity t where t.id in ?1" )
    Collection<TopicJpaEntity> findByIds(Collection<Long> ids);

}
