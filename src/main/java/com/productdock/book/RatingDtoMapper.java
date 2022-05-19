package com.productdock.book;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface RatingDtoMapper {

    BookDto.RatingDto toDto(Rating source);

}
