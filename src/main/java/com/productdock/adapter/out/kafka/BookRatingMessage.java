package com.productdock.adapter.out.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookRatingMessage implements Serializable {

    private Long bookId;
    private Double rating;
    private int ratingsCount;
}
