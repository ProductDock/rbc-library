package com.productdock.data.provider.out.postgresql;

import com.productdock.adapter.out.sql.entity.BookJpaEntity;

public class BookEntityMother {

    private static final Long defaultId = null;
    private static final String defaultTitle = "::title::";
    private static final String defaultAuthor = "::author::";
    private static final String defaultDescription = "::description::";
    private static final String defaultCover = "::cover::";

    public static BookJpaEntity.BookEntityBuilder defaultBookEntityBuilder() {
        return BookJpaEntity.builder()
                .id(defaultId)
                .title(defaultTitle)
                .author(defaultAuthor)
                .cover(defaultCover)
                .description(defaultDescription);
    }

    public static BookJpaEntity defaultBookEntity() {
        return defaultBookEntityBuilder().build();
    }

}
