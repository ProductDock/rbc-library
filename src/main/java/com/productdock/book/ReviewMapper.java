package com.productdock.book;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ReviewMapper {

    @Mapping(source = "bookId", target = "reviewCompositeKey.bookId")
    @Mapping(source = "userId", target = "reviewCompositeKey.userId")
    @Mapping(source = "recommendation", target = "recommendation", qualifiedByName = "recommendationListToIntValue")
    ReviewEntity toEntity(ReviewDto reviewDto);

    @Named("recommendationListToIntValue")
    static Integer recommendationListToIntValue(List<Recommendation> list) {

        var junior = getBitForEnum(list, Recommendation.JUNIOR);
        var medior = getBitForEnum(list, Recommendation.MEDIOR);
        var senior = getBitForEnum(list, Recommendation.SENIOR);

        var stringRepresentation = junior + medior + senior;
        return Integer.parseUnsignedInt(stringRepresentation, 2);
    }

    private static String getBitForEnum(List<Recommendation> list, Recommendation recommendation) {
        return list.contains(recommendation) ? "1" : "0";

    }

    @Mapping(source = "reviewCompositeKey.bookId", target = "bookId")
    @Mapping(source = "reviewCompositeKey.userId", target = "userId")
    @Mapping(source = "recommendation", target = "recommendation", qualifiedByName = "intValueToRecommendationList")
    ReviewDto toDto(ReviewEntity reviewEntity);

    @Named("intValueToRecommendationList")
    static List<Recommendation> intValueToRecommendationList(Integer intRepresentation) {

        var isJunior = (intRepresentation & 1) != 0;
        var isMedior = (intRepresentation & 2) != 0;
        var isSenior = (intRepresentation & 4) != 0;

        var recommendation = new ArrayList<Recommendation>();

        if (isJunior) {
            recommendation.add(Recommendation.JUNIOR);
        }
        if (isMedior) {
            recommendation.add(Recommendation.MEDIOR);
        }
        if (isSenior) {
            recommendation.add(Recommendation.SENIOR);
        }

        return recommendation;
    }

}
