package com.productdock.adapter.out.postresql.mapper;

import com.productdock.adapter.out.postresql.entity.ReviewEntity;
import com.productdock.domain.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ReviewMapper {

    @Mapping(source = "reviewCompositeKey.userId", target = "reviewCompositeKey.userId")
    @Mapping(source = "reviewCompositeKey.bookId", target = "reviewCompositeKey.bookId")
    Book.Review toDomain(ReviewEntity review);

    @Mapping(source = "reviewCompositeKey.userId", target = "reviewCompositeKey.userId")
    @Mapping(source = "reviewCompositeKey.bookId", target = "reviewCompositeKey.bookId")
    ReviewEntity toEntity(Book.Review review);

}
