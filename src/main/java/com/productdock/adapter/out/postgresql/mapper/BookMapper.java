package com.productdock.adapter.out.postgresql.mapper;


import com.productdock.adapter.out.postgresql.entity.BookEntity;
import com.productdock.adapter.out.postgresql.entity.TopicEntity;
import com.productdock.domain.Book;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {ReviewMapper.class}, builder = @Builder(disableBuilder = true))
public interface BookMapper {

    @Mapping(source = "topics", target = "topics", qualifiedByName = "topicEntitiesToTopicNameList")
    Book toDomain(BookEntity source);

    @AfterMapping
    default void setRating(@MappingTarget Book book) {
        book.calculateRating();
    }

    @Named("topicEntitiesToTopicNameList")
    static List<String> topicEntitiesToTopicNameList(Set<TopicEntity> topics) {
        return topics.stream().map(TopicEntity::getName).toList();
    }

}
