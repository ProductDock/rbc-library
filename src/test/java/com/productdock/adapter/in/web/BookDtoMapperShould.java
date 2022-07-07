package com.productdock.adapter.in.web;


import com.productdock.adapter.in.web.mapper.BookDtoMapper;
import com.productdock.adapter.in.web.mapper.BookDtoMapperImpl;
import com.productdock.adapter.in.web.mapper.ReviewDtoMapperImpl;
import com.productdock.domain.Recommendation;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.productdock.data.provider.BookMother.defaultBook;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BookDtoMapperImpl.class, ReviewDtoMapperImpl.class})
class BookDtoMapperShould {

    @Autowired
    private BookDtoMapper bookMapper;

    @Test
    void mapBookToBookDto() {
        var book = defaultBook();

        var bookDto = bookMapper.toDto(book);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(bookDto.id).isEqualTo(book.getId());
            softly.assertThat(bookDto.author).isEqualTo(book.getAuthor());
            softly.assertThat(bookDto.cover).isEqualTo(book.getCover());
            softly.assertThat(bookDto.title).isEqualTo(book.getTitle());
            softly.assertThat(bookDto.reviews).hasSize(1);
            softly.assertThat(bookDto.reviews.get(0).recommendation).containsExactlyInAnyOrder(Recommendation.JUNIOR, Recommendation.MEDIOR);
            softly.assertThat(bookDto.description).isEqualTo(book.getDescription());
        }
    }
}
