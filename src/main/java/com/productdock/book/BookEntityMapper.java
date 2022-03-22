package com.productdock.book;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface BookEntityMapper extends EntityMapper<Book, BookEntity>{

}
