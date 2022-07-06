package com.productdock.adapter.in.web.mapper;

import com.productdock.adapter.in.web.BookDto;
import com.productdock.domain.Book;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {ReviewDtoMapper.class})
public interface BookDtoMapper {

    BookDto toDto(Book source);

}
