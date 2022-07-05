package com.productdock.book;

import com.productdock.adapter.out.postresql.entity.ReviewEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ReviewMapper {

    @Mapping(source = "bookId", target = "reviewCompositeKey.bookId")
    @Mapping(source = "userId", target = "reviewCompositeKey.userId")
    @Mapping(source = "recommendation", target = "recommendation", qualifiedByName = "recommendationListToIntValue")
    ReviewEntity toEntity(ReviewDto reviewDto);

    @Named("recommendationListToIntValue")
    static Integer recommendationListToIntValue(List<Recommendation> list) {
        return RecommendationBits.from(list).toInt();
    }

    @Mapping(source = "reviewCompositeKey.bookId", target = "bookId")
    @Mapping(source = "reviewCompositeKey.userId", target = "userId")
    @Mapping(source = "recommendation", target = "recommendation", qualifiedByName = "intValueToRecommendationList")
    ReviewDto toDto(ReviewEntity reviewEntity);

    @Named("intValueToRecommendationList")
    static List<Recommendation> intValueToRecommendationList(Integer intRepresentation) {
        return new RecommendationBits(intRepresentation).toList();
    }
}
