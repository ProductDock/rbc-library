package com.productdock.data.provider.provider;

import com.productdock.adapter.out.postresql.entity.BookEntity;
import com.productdock.adapter.out.postresql.entity.ReviewEntity;

import java.util.List;

import static com.productdock.data.provider.provider.ReviewEntityMother.defaultReviewEntity;

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

    public static BookEntity.BookEntityBuilder bookWithAnyCover() {
        return BookEntity.builder()
                .id(defaultId)
                .title(defaultTitle)
                .author(defaultAuthor)
                .cover("http://cover")
                .description(defaultDescription);
    }

}
