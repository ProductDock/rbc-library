package com.productdock.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookEntityMapper bookEntityMapper;

    public List<Book> getAll() {
        return bookRepository
                .findAll()
                .stream()
                .map(bookEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

}
