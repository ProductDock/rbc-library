package com.productdock.book;


import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.productdock.book.data.provider.BookEntityMother.defaultBookBuilder;
import static com.productdock.book.data.provider.ReviewEntityMother.defaultReviewEntity;
import static com.productdock.book.data.provider.TopicEntityMother.defaultTopic;
import static com.productdock.book.data.provider.TopicEntityMother.defaultTopicBuilder;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BookMapperImpl.class, ReviewDtoMapperImpl.class})
class BookMapperShould {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private ReviewDtoMapper reviewDtoMapper;

    @Test
    void mapBookEntityToBookDto() {
        var reviewEntity = defaultReviewEntity();
        var bookEntity = defaultBookBuilder().review(reviewEntity).build();

        var bookDto = bookMapper.toDto(bookEntity);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(bookDto.id).isEqualTo(bookEntity.getId());
            softly.assertThat(bookDto.author).isEqualTo(bookEntity.getAuthor());
            softly.assertThat(bookDto.cover).isEqualTo(bookEntity.getCover());
            softly.assertThat(bookDto.title).isEqualTo(bookEntity.getTitle());
            softly.assertThat(bookDto.reviews).hasSize(1);
            softly.assertThat(bookDto.description).isEqualTo(bookEntity.getDescription());
        }
    }

    @Test
    void mapBookEntityToBookDto_whenTopicsPresent() {
        var topic1 = defaultTopic();
        var topic2 = defaultTopicBuilder().name("::topic-2::").build();
        var bookEntity = defaultBookBuilder().topic(topic1).topic(topic2).build();

        var bookDto = bookMapper.toDto(bookEntity);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(bookDto.topics).containsExactlyInAnyOrder(topic1.getName(), topic2.getName());
        }
    }
}
