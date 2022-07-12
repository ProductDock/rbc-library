package com.productdock.application.port.in;

import com.productdock.domain.Book;

public interface GetBookQuery {

    Book getById(Long bookId);

}
