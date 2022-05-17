package com.productdock.book;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {ReviewDtoMapper.class})
public interface BookMapper {

    BookDto toDto(BookEntity source);
}
