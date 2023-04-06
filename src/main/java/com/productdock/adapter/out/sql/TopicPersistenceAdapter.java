package com.productdock.adapter.out.sql;

import com.productdock.adapter.out.sql.mapper.TopicMapper;
import com.productdock.application.port.out.persistence.TopicPersistenceOutPort;
import com.productdock.domain.Book;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
@Slf4j
@AllArgsConstructor
public class TopicPersistenceAdapter implements TopicPersistenceOutPort {

    private TopicRepository topicRepository;

    private TopicMapper topicMapper;

    @Override
    public Collection<Book.Topic> findByIds(Collection<Long> ids) {
        return topicMapper.toDomainCollection(topicRepository.findByIds(ids));
    }

    @Override
    public Collection<Book.Topic> findAll() {
        return topicMapper.toDomainCollection(topicRepository.findAll());
    }
}
