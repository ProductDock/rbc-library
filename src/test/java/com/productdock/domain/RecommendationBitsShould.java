package com.productdock.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.productdock.domain.Recommendation.*;
import static org.assertj.core.api.Assertions.assertThat;

class RecommendationBitsShould {

    @ParameterizedTest
    @MethodSource("recommendationListParametersProvider")
    void convertRecommendationListToInt(List<Recommendation> recommendations, int output) {
        assertThat(getIntRepresentation(recommendations)).isEqualTo(output);
    }

    static Stream<Arguments> recommendationListParametersProvider() {
        return Stream.of(
                Arguments.of(List.of(JUNIOR), 1),
                Arguments.of(List.of(MEDIOR), 2),
                Arguments.of(List.of(SENIOR), 4),
                Arguments.of(List.of(JUNIOR, MEDIOR), 3),
                Arguments.of(List.of(SENIOR, MEDIOR), 6),
                Arguments.of(List.of(SENIOR, JUNIOR, MEDIOR), 7)
        );
    }

    private int getIntRepresentation(List<Recommendation> recommendationList) {
        return RecommendationBits.from(recommendationList).toInt();
    }

    @ParameterizedTest
    @MethodSource("recommendationBitsParametersProvider")
    void convertIntToRecommendationList(int intRepresentation, List<Recommendation> output) {
        assertThat(getRecommendations(intRepresentation)).containsExactlyInAnyOrderElementsOf(output);
    }

    static Stream<Arguments> recommendationBitsParametersProvider() {
        return Stream.of(
                Arguments.of(1, List.of(JUNIOR)),
                Arguments.of(2, List.of(MEDIOR)),
                Arguments.of(4, List.of(SENIOR)),
                Arguments.of(3, List.of(JUNIOR, MEDIOR)),
                Arguments.of(6, List.of(SENIOR, MEDIOR)),
                Arguments.of(7, List.of(SENIOR, JUNIOR, MEDIOR))
        );
    }

    private List<Recommendation> getRecommendations(int intRepresentation) {
        return new RecommendationBits(intRepresentation).toList();
    }

}
