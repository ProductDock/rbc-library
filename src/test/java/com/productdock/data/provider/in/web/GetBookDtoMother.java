package com.productdock.data.provider.in.web;

import com.productdock.adapter.in.web.dto.GetBookDto;

public class GetBookDtoMother {
    private static final Long defaultId = 1L;
    private static final String defaultTitle = "::title::";
    private static final String defaultAuthor = "::author::";
    private static final String defaultDescription = "::description::";
    private static final String defaultCover = "::cover::";

    public static GetBookDto.GetBookDtoBuilder defaultGetBookDtoBuilder() {
        return GetBookDto.builder()
                .id(defaultId)
                .title(defaultTitle)
                .author(defaultAuthor)
                .cover(defaultCover)
                .description(defaultDescription);
    }

    public static GetBookDto defaultGetBookDto() {
        return defaultGetBookDtoBuilder().build();
    }
}
