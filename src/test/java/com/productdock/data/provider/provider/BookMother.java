package com.productdock.data.provider.provider;

import com.productdock.domain.Book;

import java.util.List;

import static com.productdock.data.provider.provider.ReviewMother.defaultReview;

public class BookMother {
    private static final Long defaultId = null;
    private static final String defaultTitle = "::title::";
    private static final String defaultAuthor = "::author::";
    private static final String defaultDescription = "::description::";
    private static final String defaultCover = null;
    private static final List<String> defaultTopics = List.of("::topic::");
    private static final List<Book.Review> defaultReviews = List.of(defaultReview());

    public static Book.BookBuilder defaultBookBuilder() {
        return Book.builder()
                .id(defaultId)
                .title(defaultTitle)
                .author(defaultAuthor)
                .cover(defaultCover)
                .description(defaultDescription)
                .reviews(defaultReviews)
                .topics(defaultTopics);
    }

    public static Book defaultBook() {
        return defaultBookBuilder().build();
    }
}
