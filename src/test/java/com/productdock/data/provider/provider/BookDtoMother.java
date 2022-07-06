package com.productdock.data.provider.provider;

import com.productdock.adapter.in.web.BookDto;

public class BookDtoMother {
    private static final Long defaultId = 1L;
    private static final String defaultTitle = "::title::";
    private static final String defaultAuthor = "::author::";
    private static final String defaultDescription = "::description::";
    private static final String defaultCover = "::cover::";

    public static BookDto.BookDtoBuilder defaultBookDtoBuilder() {
        return BookDto.builder()
                .id(defaultId)
                .title(defaultTitle)
                .author(defaultAuthor)
                .cover(defaultCover)
                .description(defaultDescription);
    }

    public static BookDto defaultBookDto() {
        return defaultBookDtoBuilder().build();
    }
}
