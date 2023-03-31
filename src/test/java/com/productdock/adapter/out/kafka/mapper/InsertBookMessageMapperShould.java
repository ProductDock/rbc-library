package com.productdock.adapter.out.kafka.mapper;

import com.productdock.adapter.in.web.mapper.Recommendation;
import com.productdock.domain.Book;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.productdock.data.provider.domain.BookMother.defaultBook;
import static com.productdock.data.provider.domain.BookMother.defaultBookBuilder;
import static com.productdock.data.provider.domain.TopicMother.defaultTopic;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

class InsertBookMessageMapperShould {

    private InsertBookMessageMapper insertBookMessageMapper = Mappers.getMapper(InsertBookMessageMapper.class);

    @Test
    void mapBookToInsertBookMessage() {
        var topic = defaultTopic();
        var book = defaultBookBuilder().topic(topic).build();
        var bookCopies = 2;

        var insertBookMessage = insertBookMessageMapper.toMessage(book, bookCopies);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(insertBookMessage.getBookId()).isEqualTo(book.getId());
            softly.assertThat(insertBookMessage.getAuthor()).isEqualTo(book.getAuthor());
            softly.assertThat(insertBookMessage.getCover()).isEqualTo(book.getCover());
            softly.assertThat(insertBookMessage.getTitle()).isEqualTo(book.getTitle());
            softly.assertThat(insertBookMessage.getBookCopies()).isEqualTo(bookCopies);

            softly.assertThat(insertBookMessage.getTopics())
                    .extracting("id", "name")
                    .containsExactlyInAnyOrder(tuple(topic.getId(), topic.getName()));
        }

    }
}
