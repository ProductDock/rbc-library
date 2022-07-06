package com.productdock.adapter.out.postresql;

import com.productdock.adapter.out.postresql.entity.BookEntity;
import com.productdock.adapter.out.postresql.mapper.BookMapper;
import com.productdock.application.port.out.persistence.BookPersistenceOutPort;
import com.productdock.domain.Book;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Slf4j
@AllArgsConstructor
public class BookRepository implements BookPersistenceOutPort {

    private BookJpaRepository jpaRepository;
    private BookMapper bookMapper;

    @Override
    public Optional<Book> findById(Long bookId){
        Optional<BookEntity> bookEntity = jpaRepository.findById(bookId);
        if (bookEntity.isEmpty()) {
            log.debug("Unable to find a book with book id: {}", bookId);
            return Optional.empty();
        }
        return Optional.of(bookMapper.toDomain(bookEntity.get()));
    }

}
