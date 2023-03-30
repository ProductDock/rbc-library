package com.productdock.adapter.in.web.mapper;

import com.productdock.adapter.in.web.dto.InsertBookDto;
import com.productdock.domain.Book;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface InsertBookDtoMapper {

    Book toDomain(InsertBookDto source);
}
