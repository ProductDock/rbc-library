package com.productdock.adapter.out.sql;

import com.productdock.adapter.out.sql.entity.BookJpaEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BookRepository extends PagingAndSortingRepository<BookJpaEntity, Long> {

    BookJpaEntity findByTitleAndAuthor(String title, String author);
}


