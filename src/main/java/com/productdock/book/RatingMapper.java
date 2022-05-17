package com.productdock.book;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface RatingMapper {

    BookDto.RatingDto toDto(Rating source);

}
