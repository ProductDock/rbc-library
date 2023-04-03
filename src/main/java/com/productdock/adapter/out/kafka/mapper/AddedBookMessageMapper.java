package com.productdock.adapter.out.kafka.mapper;

import com.productdock.adapter.out.kafka.messages.AddedBookMessage;
import com.productdock.domain.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface AddedBookMessageMapper {

    @Mapping(source = "bookCopies", target = "bookCopies")
    @Mapping(source = "source.id", target = "bookId")
    AddedBookMessage toMessage(Book source, int bookCopies);

}
