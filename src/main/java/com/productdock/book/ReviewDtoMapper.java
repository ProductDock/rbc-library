package com.productdock.book;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ReviewDtoMapper {

    @Mapping(source = "recommendation", target = "recommendation", qualifiedByName = "intValueToRecommendationList")
    BookDto.ReviewDto toDto(ReviewEntity review);

    @Named("intValueToRecommendationList")
    static List<Recommendation> intValueToRecommendationList(Integer intRepresentation) {
        return new RecommendationBits(intRepresentation).toList();
    }
}
