package com.productdock.adapter.in.web.mapper;

import com.productdock.adapter.in.web.dto.GetBookDto;
import com.productdock.domain.Book;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Collection;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface TopicDtoMapper {

    Collection<GetBookDto.TopicDto> toDtoCollection(Collection<Book.Topic> source);
}
