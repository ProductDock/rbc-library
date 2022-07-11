package com.productdock.adapter.out.sql.mapper;


import com.productdock.adapter.out.sql.entity.ReviewJpaEntity;
import com.productdock.domain.Book;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ReviewCompositeKeyMapper {

    ReviewJpaEntity.ReviewCompositeKey toEntity(Book.Review.ReviewCompositeKey source);

}
