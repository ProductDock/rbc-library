package com.productdock.application.service;

import com.productdock.application.port.in.GetTopicQuery;
import com.productdock.application.port.out.persistence.TopicPersistenceOutPort;
import com.productdock.domain.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Slf4j
record GetTopicService(TopicPersistenceOutPort topicRepository) implements GetTopicQuery {

    @Override
    public Collection<Book.Topic> getAll() {
        log.debug("Fetched all topics");
        return topicRepository.findAll();
    }
}
