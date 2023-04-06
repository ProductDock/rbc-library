package com.productdock.adapter.in.web.mapper;


import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.productdock.data.provider.domain.BookMother.defaultBookBuilder;
import static com.productdock.data.provider.domain.ReviewMother.defaultReview;
import static com.productdock.data.provider.domain.TopicMother.defaultTopic;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {GetBookDtoMapperImpl.class, ReviewDtoMapperImpl.class})
class GetBookDtoMapperShould {

    @Autowired
    private GetBookDtoMapper bookMapper;

    @Test
    void mapBookToBookDto() {
        var review = defaultReview();
        var topic = defaultTopic();
        var book = defaultBookBuilder().review(review).topic(topic).build();

        var bookDto = bookMapper.toDto(book);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(bookDto.id).isEqualTo(book.getId());
            softly.assertThat(bookDto.author).isEqualTo(book.getAuthor());
            softly.assertThat(bookDto.cover).isEqualTo(book.getCover());
            softly.assertThat(bookDto.title).isEqualTo(book.getTitle());
            softly.assertThat(bookDto.reviews).hasSize(1);
            softly.assertThat(bookDto.reviews)
                    .flatExtracting("recommendation")
                    .containsExactlyInAnyOrder(Recommendation.JUNIOR, Recommendation.MEDIOR);
            softly.assertThat(bookDto.description).isEqualTo(book.getDescription());
            softly.assertThat(bookDto.topics)
                    .extracting("id", "name")
                    .containsExactlyInAnyOrder(tuple(topic.getId(), topic.getName()));
        }
    }
}
