package com.productdock.adapter.out.sql;

import com.productdock.adapter.out.sql.entity.TopicJpaEntity;
import com.productdock.adapter.out.sql.mapper.BookMapper;
import com.productdock.application.port.out.persistence.BookPersistenceOutPort;
import com.productdock.domain.Book;
import com.productdock.domain.exception.SaveBookException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Slf4j
@AllArgsConstructor
class BookPersistenceAdapter implements BookPersistenceOutPort {

    private BookRepository bookRepository;

    private TopicRepository topicRepository;
    private BookMapper bookMapper;

    @Override
    public Optional<Book> findById(Long bookId) {
        return bookRepository.findById(bookId).map(bookJpaEntity -> bookMapper.toDomain(bookJpaEntity));
    }

    @Override
    public Optional<Book> findByTitleAndAuthor(String title, String author) {
        var bookJpaEntity = bookRepository.findByTitleAndAuthor(title, author);
        if (bookJpaEntity == null) {
            return Optional.empty();
        }
        return Optional.of(bookMapper.toDomain(bookJpaEntity));
    }

    @Override
    public Book save(Book book) {
        var bookJpaEntity = bookMapper.toEntity(book);
        bookJpaEntity.setTopics(populateBookTopics(book.getTopics()));
        return bookMapper.toDomain(bookRepository.save(bookJpaEntity));
    }

    private Set<TopicJpaEntity> populateBookTopics(List<Book.Topic> topics) {
        var topicEntities = topicRepository.findByIds(topics.stream().map(Book.Topic::getId).toList());
        if (topics.size() != topicEntities.size())
            throw new SaveBookException("Provided topic ids are not valid");
        return new HashSet<>(topicEntities);
    }

}
