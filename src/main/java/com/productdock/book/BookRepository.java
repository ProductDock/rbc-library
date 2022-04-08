package com.productdock.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BookRepository extends PagingAndSortingRepository<BookEntity, Long> {

    @Query("""
        select b from BookEntity b
        left join b.topics t
        where t.name in :topics
        """)
    Page<BookEntity> findAllByTopicsName(List<String> topics, Pageable pageable);
}


