package com.productdock.adapter.in.web;

import org.junit.jupiter.api.Test;

import static com.productdock.adapter.in.web.mapper.Recommendation.*;
import static org.assertj.core.api.Assertions.assertThat;

class RecommendationShould {

    @Test
    void returnAppropriateBit() {

        assertThat(JUNIOR.getBit()).isEqualTo(1);
        assertThat(MEDIOR.getBit()).isEqualTo(2);
        assertThat(SENIOR.getBit()).isEqualTo(4);

    }
}
