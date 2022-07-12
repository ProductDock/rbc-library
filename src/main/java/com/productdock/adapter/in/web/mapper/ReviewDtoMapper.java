package com.productdock.adapter.in.web.mapper;

import com.productdock.adapter.in.web.ReviewDto;
import com.productdock.domain.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ReviewDtoMapper {

    @Mapping(source = "bookId", target = "reviewCompositeKey.bookId")
    @Mapping(source = "userId", target = "reviewCompositeKey.userId")
    @Mapping(source = "recommendation", target = "recommendation", qualifiedByName = "recommendationListToIntValue")
    Book.Review toDomain(ReviewDto reviewDto);

    @Named("recommendationListToIntValue")
    static Integer recommendationListToIntValue(List<Recommendation> list) {
        return RecommendationBits.from(list).toInt();
    }

    @Mapping(source = "recommendation", target = "recommendation", qualifiedByName = "intValueToRecommendationList")
    @Mapping(source = "reviewCompositeKey.userId", target = "userId")
    @Mapping(source = "reviewCompositeKey.bookId", target = "bookId")
    ReviewDto toDto(Book.Review review);

    @Named("intValueToRecommendationList")
    static List<Recommendation> intValueToRecommendationList(Integer intRepresentation) {
        return new RecommendationBits(intRepresentation).toList();
    }
}
