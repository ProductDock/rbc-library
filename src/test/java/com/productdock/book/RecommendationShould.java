package com.productdock.book;

import org.junit.jupiter.api.Test;

import static com.productdock.book.Recommendation.*;
import static org.assertj.core.api.Assertions.assertThat;

class RecommendationShould {

    @Test
    void returnAppropriateBit() {

        assertThat(JUNIOR.getBit()).isEqualTo(1);
        assertThat(MEDIOR.getBit()).isEqualTo(2);
        assertThat(SENIOR.getBit()).isEqualTo(4);

    }
}
