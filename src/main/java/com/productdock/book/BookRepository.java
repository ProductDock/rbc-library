package com.productdock.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BookRepository extends PagingAndSortingRepository<BookEntity, Long> {

    @Query( value= "select b.* from book b\r\n " +
                    "left join book_topic bt on b.id = bt.book_id\r\n " +
                    "left join topic t on t.id = bt.topic_id\r\n" +
                    "where t.name in (?1)",
    nativeQuery = true)
    Page<BookEntity> findAllByTopics_Name(List<String> filters, Pageable pageable);
}
