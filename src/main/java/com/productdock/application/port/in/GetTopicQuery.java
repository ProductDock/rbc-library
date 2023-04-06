package com.productdock.application.port.in;

import com.productdock.domain.Book;

import java.util.Collection;

public interface GetTopicQuery {

    Collection<Book.Topic> getAll();
}
