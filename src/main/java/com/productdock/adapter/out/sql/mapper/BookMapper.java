package com.productdock.adapter.out.sql.mapper;


import com.productdock.adapter.out.sql.entity.BookJpaEntity;
import com.productdock.domain.Book;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {ReviewMapper.class}, builder = @Builder(disableBuilder = true))
public interface BookMapper {

    Book toDomain(BookJpaEntity source);

    BookJpaEntity toEntity(Book book);

}
