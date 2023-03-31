package com.productdock.application.port.in;

import com.productdock.domain.Book;

public interface SaveBookUseCase {

    Long saveBook(Book book, int bookCopies);
}
