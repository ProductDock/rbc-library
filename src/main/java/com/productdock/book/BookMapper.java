package com.productdock.book;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {ReviewDtoMapper.class})
public interface BookMapper {

    @Mapping(source = "topics", target = "topics", qualifiedByName = "topicEntitiesToTopicNameList")
    BookDto toDto(BookEntity source);

    @Named("topicEntitiesToTopicNameList")
    static List<String> topicEntitiesToTopicNameList(Set<TopicEntity> topics) {
        return topics.stream().map(TopicEntity::getName).toList();
    }
}
