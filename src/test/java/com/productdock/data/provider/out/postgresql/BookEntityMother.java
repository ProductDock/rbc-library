package com.productdock.data.provider.out.postgresql;

import com.productdock.adapter.out.sql.entity.BookEntity;

public class BookEntityMother {

    private static final Long defaultId = null;
    private static final String defaultTitle = "::title::";
    private static final String defaultAuthor = "::author::";
    private static final String defaultDescription = "::description::";
    private static final String defaultCover = "::cover::";

    public static BookEntity.BookEntityBuilder defaultBookEntityBuilder() {
        return BookEntity.builder()
                .id(defaultId)
                .title(defaultTitle)
                .author(defaultAuthor)
                .cover(defaultCover)
                .description(defaultDescription);
    }

    public static BookEntity defaultBookEntity() {
        return defaultBookEntityBuilder().build();
    }

}
