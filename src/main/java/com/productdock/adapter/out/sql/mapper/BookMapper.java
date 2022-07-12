package com.productdock.adapter.out.sql.mapper;


import com.productdock.adapter.out.sql.entity.BookJpaEntity;
import com.productdock.adapter.out.sql.entity.TopicJpaEntity;
import com.productdock.domain.Book;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {ReviewMapper.class}, builder = @Builder(disableBuilder = true))
public interface BookMapper {

    @Mapping(source = "topics", target = "topics", qualifiedByName = "topicEntitiesToTopicNameList")
    Book toDomain(BookJpaEntity source);

    @Named("topicEntitiesToTopicNameList")
    static List<String> topicEntitiesToTopicNameList(Set<TopicJpaEntity> topics) {
        return topics.stream().map(TopicJpaEntity::getName).toList();
    }

}
