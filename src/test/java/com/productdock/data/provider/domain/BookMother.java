package com.productdock.data.provider.domain;

import com.productdock.domain.Book;

public class BookMother {
    private static final Long defaultId = null;
    private static final String defaultTitle = "::title::";
    private static final String defaultAuthor = "::author::";
    private static final String defaultDescription = "::description::";
    private static final String defaultCover = null;

    public static Book.BookBuilder defaultBookBuilder() {
        return Book.builder()
                .id(defaultId)
                .title(defaultTitle)
                .author(defaultAuthor)
                .cover(defaultCover)
                .description(defaultDescription);
    }

    public static Book defaultBook() {
        return defaultBookBuilder().build();
    }
}
