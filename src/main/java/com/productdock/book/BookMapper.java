package com.productdock.book;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {ReviewDtoMapper.class})
public interface BookMapper {

    @Mapping(target = "reviews", source = "source.reviews")
    BookDto toDto(BookEntity source);

}
