package com.productdock.adapter.out.sql;

import com.productdock.adapter.out.sql.mapper.BookMapper;
import com.productdock.application.port.out.persistence.BookPersistenceOutPort;
import com.productdock.domain.Book;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Slf4j
@AllArgsConstructor
public class BookPersistenceAdapter implements BookPersistenceOutPort {

    private BookRepository bookRepository;
    private BookMapper bookMapper;

    @Override
    public Optional<Book> findById(Long bookId) {
        return bookRepository.findById(bookId).map(bookJpaEntity -> bookMapper.toDomain(bookJpaEntity));
    }

}
