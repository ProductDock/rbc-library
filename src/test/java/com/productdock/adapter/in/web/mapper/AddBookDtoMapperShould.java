package com.productdock.adapter.in.web.mapper;

import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.productdock.data.provider.in.web.AddBookDtoMother.defaultAddBookDtoBuilder;
import static com.productdock.data.provider.in.web.AddBookDtoMother.defaultTopicDto;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

class AddBookDtoMapperShould {

    private AddBookDtoMapper addBookDtoMapper = Mappers.getMapper(AddBookDtoMapper.class);

    @Test
    void mapInsertBookDtoToDomain() {
        var topicDto = defaultTopicDto();
        var insertBookDto = defaultAddBookDtoBuilder().topic(topicDto).build();

        var book = addBookDtoMapper.toDomain(insertBookDto);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(book.getAuthor()).isEqualTo(insertBookDto.author);
            softly.assertThat(book.getCover()).isEqualTo(insertBookDto.cover);
            softly.assertThat(book.getTitle()).isEqualTo(insertBookDto.title);
            softly.assertThat(book.getDescription()).isEqualTo(insertBookDto.description);
            softly.assertThat(book.getTopics())
                    .extracting("id", "name")
                    .containsExactlyInAnyOrder(tuple(topicDto.id, null));
        }
    }

}
