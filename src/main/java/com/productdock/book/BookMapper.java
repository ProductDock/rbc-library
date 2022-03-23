package com.productdock.book;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.awt.print.Book;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BookMapper {

    BookDto toDto(BookEntity bookEntity);

}
