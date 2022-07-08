package com.productdock.adapter.out.postgresql.mapper;

import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.productdock.data.provider.out.postgresql.BookEntityMother.defaultBookEntityBuilder;
import static com.productdock.data.provider.out.postgresql.ReviewEntityMother.defaultReviewEntity;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BookMapperImpl.class, ReviewMapperImpl.class})
class BookMapperShould {

    @Autowired
    private BookMapper bookMapper;

    @Test
    void mapBookEntityToBook() {
        var reviews = List.of(defaultReviewEntity());
        var bookEntity = defaultBookEntityBuilder().reviews(reviews).build();

        var book = bookMapper.toDomain(bookEntity);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(book.getId()).isEqualTo(bookEntity.getId());
            softly.assertThat(book.getAuthor()).isEqualTo(bookEntity.getAuthor());
            softly.assertThat(book.getCover()).isEqualTo(bookEntity.getCover());
            softly.assertThat(book.getTitle()).isEqualTo(bookEntity.getTitle());
            softly.assertThat(book.getReviews()).hasSize(1);
            softly.assertThat(book.getRating().getCount()).isEqualTo(1);
            softly.assertThat(book.getRating().getScore()).isEqualTo(2);
            softly.assertThat(book.getDescription()).isEqualTo(bookEntity.getDescription());
        }
    }
}
