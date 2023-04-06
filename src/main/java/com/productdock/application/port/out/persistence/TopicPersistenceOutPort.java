package com.productdock.application.port.out.persistence;

import com.productdock.domain.Book;

import java.util.Collection;

public interface TopicPersistenceOutPort {

    Collection<Book.Topic> findByIds(Collection<Long> ids);

    Collection<Book.Topic> findAll();

}
