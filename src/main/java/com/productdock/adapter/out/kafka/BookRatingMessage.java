package com.productdock.adapter.out.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@ToString
public class BookRatingMessage implements Serializable {

    public final Long bookId;
    public final Double rating;
    public final int ratingsCount;
}
