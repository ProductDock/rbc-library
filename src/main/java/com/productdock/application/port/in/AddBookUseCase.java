package com.productdock.application.port.in;

import com.productdock.domain.Book;

public interface AddBookUseCase {

    Long addBook(Book book, int bookCopies);
}
