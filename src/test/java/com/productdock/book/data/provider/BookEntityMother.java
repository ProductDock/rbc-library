package com.productdock.book.data.provider;

import com.productdock.book.BookEntity;

public class BookEntityMother {

    private static final Long defaultId = null;
    private static final String defaultTitle = "::title::";
    private static final String defaultAuthor = "::author::";
    private static final String defaultCover = null;
    
    public static BookEntity.BookEntityBuilder defaultBookBuilder() {
        return BookEntity.builder()
                .id(defaultId)
                .title(defaultTitle)
                .author(defaultAuthor)
                .cover(defaultCover);
    }

    public static BookEntity defaultBook() {
        return defaultBookBuilder().build();
    }
    
    
}