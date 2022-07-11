package com.productdock.adapter.out.sql.mapper;


import com.productdock.adapter.out.sql.entity.BookEntity;
import com.productdock.adapter.out.sql.entity.TopicEntity;
import com.productdock.domain.Book;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {ReviewMapper.class}, builder = @Builder(disableBuilder = true))
public interface BookMapper {

    @Mapping(source = "topics", target = "topics", qualifiedByName = "topicEntitiesToTopicNameList")
    Book toDomain(BookEntity source);

    @Named("topicEntitiesToTopicNameList")
    static List<String> topicEntitiesToTopicNameList(Set<TopicEntity> topics) {
        return topics.stream().map(TopicEntity::getName).toList();
    }

}
