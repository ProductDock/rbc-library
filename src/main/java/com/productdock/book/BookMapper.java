package com.productdock.book;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BookMapper {

    Book toDomain(BookEntity bookEntity);

    BookDto toDto(Book book);

}
