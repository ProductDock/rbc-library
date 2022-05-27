package com.productdock.book;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookService {

    private BookRepository bookRepository;
    private BookMapper bookMapper;
    private RatingDtoMapper ratingDtoMapper;
    private BookRatingCalculator bookRatingCalculator;

    static Logger logger = LoggerFactory.getLogger(BookService.class);

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
        logger.info("Fetched book with book id: {}", bookId);
        Optional<BookEntity> book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            return null;
        }
        var rating = bookRatingCalculator.calculate(book.get().getReviews());
        var bookDto = bookMapper.toDto(book.get());
        bookDto.rating = ratingDtoMapper.toDto(rating);
        return bookDto;
    }
}
