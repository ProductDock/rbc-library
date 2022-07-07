package com.productdock.adapter.in.web.mapper;

import java.util.List;

import static java.util.Arrays.asList;

public class RecommendationBits {
    private static final List<Recommendation> KNOWN_RECOMMENDATIONS = asList(Recommendation.values());
    private int intRepresentation;

    public static RecommendationBits from(List<Recommendation> recommendations) {
        RecommendationBits bits = RecommendationBits.noBitsSet();
        for (Recommendation r : recommendations) {
            bits.add(r);
        }
        return bits;
    }

    public List<Recommendation> toList() {
        return KNOWN_RECOMMENDATIONS.stream()
                .filter(this::hasRecommendation)
                .toList();
    }

    private static RecommendationBits noBitsSet() {
        return new RecommendationBits();
    }

    private RecommendationBits() {
    }

    public RecommendationBits(int intRepresentation) {
        this.intRepresentation = intRepresentation;
    }

    public void add(Recommendation recommendation) {
        this.intRepresentation |= recommendation.getBit();
    }

    public boolean hasRecommendation(Recommendation recommendation) {
        return (this.intRepresentation & recommendation.getBit()) != 0;
    }

    public int toInt() {
        return intRepresentation;
    }
}
