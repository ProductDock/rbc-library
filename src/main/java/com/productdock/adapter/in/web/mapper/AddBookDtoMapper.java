package com.productdock.adapter.in.web.mapper;

import com.productdock.adapter.in.web.dto.AddBookDto;
import com.productdock.domain.Book;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface AddBookDtoMapper {

    Book toDomain(AddBookDto source);
}
