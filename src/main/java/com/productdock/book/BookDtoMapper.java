package com.productdock.book;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface BookDtoMapper extends DtoMapper<BookDto, Book> {
}
