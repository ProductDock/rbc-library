package com.productdock.book;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.productdock.book.Recommendation.*;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

class RecommendationBitsShould {

    @Test
    void convertRecommendationListToInt() {

        var recommendations = singletonList(SENIOR);
        assertThat(getIntRepresentation(recommendations)).isEqualTo(4);

        recommendations = List.of(SENIOR, JUNIOR, MEDIOR);
        assertThat(getIntRepresentation(recommendations)).isEqualTo(7);
    }

    private int getIntRepresentation(List<Recommendation> recommendationList) {
        return RecommendationBits.from(recommendationList).toInt();
    }

    @Test
    void convertIntToRecommendationList() {

        assertThat(getRecommendations(7)).containsExactlyInAnyOrder(JUNIOR, MEDIOR, SENIOR);
        assertThat(getRecommendations(1)).containsExactly(JUNIOR);
    }

    private List<Recommendation> getRecommendations(int intRepresentation) {
        return new RecommendationBits(intRepresentation).toList();
    }

}
