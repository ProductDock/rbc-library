package com.productdock.adapter.out.sql;

import com.productdock.adapter.out.sql.entity.BookEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BookJpaRepository extends PagingAndSortingRepository<BookEntity, Long> {

}


