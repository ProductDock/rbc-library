package com.productdock.data.provider.in.web;

import com.productdock.adapter.in.web.dto.InsertBookDto;

public class InsertBookDtoMother {
    private static final Integer defaultBookCopies = 2;
    private static final String defaultTitle = "::title::";
    private static final String defaultAuthor = "::author::";
    private static final String defaultDescription = "::description::";
    private static final String defaultCover = "::cover::";
    private static final Long defaultTopicId = 1L;
    private static final String defaultTopicName = "::topicName::";

    public static InsertBookDto.InsertBookDtoBuilder defaultInsertBookDtoBuilder() {
        return InsertBookDto.builder()
                .cover(defaultCover)
                .title(defaultTitle)
                .author(defaultAuthor)
                .description(defaultDescription)
                .bookCopies(defaultBookCopies);
    }

    public static InsertBookDto defaultInsertBookDto() {
        return defaultInsertBookDtoBuilder().build();
    }

    public static InsertBookDto.TopicDto defaultTopicDto() {
        return InsertBookDto.TopicDto.builder()
                .id(defaultTopicId).build();

    }

}
