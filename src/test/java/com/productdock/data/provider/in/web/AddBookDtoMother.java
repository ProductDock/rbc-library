package com.productdock.data.provider.in.web;

import com.productdock.adapter.in.web.dto.AddBookDto;

public class AddBookDtoMother {
    private static final Integer defaultBookCopies = 2;
    private static final String defaultTitle = "::title::";
    private static final String defaultAuthor = "::author::";
    private static final String defaultDescription = "::description::";
    private static final String defaultCover = "::cover::";
    private static final Long defaultTopicId = 1L;
    private static final String defaultTopicName = "::topicName::";

    public static AddBookDto.AddBookDtoBuilder defaultAddBookDtoBuilder() {
        return AddBookDto.builder()
                .cover(defaultCover)
                .title(defaultTitle)
                .author(defaultAuthor)
                .description(defaultDescription)
                .bookCopies(defaultBookCopies);
    }

    public static AddBookDto defaultAddBookDto() {
        return defaultAddBookDtoBuilder().build();
    }

    public static AddBookDto.TopicDto defaultTopicDto() {
        return AddBookDto.TopicDto.builder()
                .id(defaultTopicId).build();

    }

}
