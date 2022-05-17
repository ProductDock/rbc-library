package com.productdock.book;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookService {

    private BookRepository bookRepository;
    private BookMapper bookMapper;


    private static final int PAGE_SIZE = 18;

    public SearchBooksResponse getBooks(Optional<List<String>> topics, int page) {
        var pageTemplate = PageRequest.of(page, PAGE_SIZE);

        Page<BookEntity> booksPage = bookRepository
                .findByTopicsName(topics, pageTemplate);

        List<BookDto> books = booksPage
                .stream()
                .map(bookMapper::toDto)
                .toList();
        return new SearchBooksResponse(booksPage.getTotalElements(), books);
    }

    public BookDto findById(Long bookId) {
        Optional<BookEntity> book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            return null;
        }
        var bookDto = bookMapper.toDto(book.get());
        bookDto.rating = BookRatingCalculator.calculateBookRating(bookDto.reviews);
        return bookDto;
    }
}
