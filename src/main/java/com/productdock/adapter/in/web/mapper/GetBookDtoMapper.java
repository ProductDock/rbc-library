package com.productdock.adapter.in.web.mapper;

import com.productdock.adapter.in.web.dto.GetBookDto;
import com.productdock.domain.Book;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {ReviewDtoMapper.class})
public interface GetBookDtoMapper {

    GetBookDto toDto(Book source);

}
