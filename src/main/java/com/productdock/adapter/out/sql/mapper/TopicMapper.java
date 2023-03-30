package com.productdock.adapter.out.sql.mapper;

import com.productdock.adapter.out.sql.entity.TopicJpaEntity;
import com.productdock.domain.Book;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Collection;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface TopicMapper {

    Collection<Book.Topic> toDomainCollection(Collection<TopicJpaEntity> source);

}
