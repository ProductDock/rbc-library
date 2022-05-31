package com.productdock.book;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class BookService {

    private BookRepository bookRepository;
    private BookMapper bookMapper;
    private RatingDtoMapper ratingDtoMapper;
    private BookRatingCalculator bookRatingCalculator;

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
            log.debug("Unable to find a book with book id: {}", bookId);
            return null;
        }
        var rating = bookRatingCalculator.calculate(book.get().getReviews());
        var bookDto = bookMapper.toDto(book.get());
        bookDto.rating = ratingDtoMapper.toDto(rating);
        log.debug("Fetched book with book id: {}", bookId);
        return bookDto;
    }
}
