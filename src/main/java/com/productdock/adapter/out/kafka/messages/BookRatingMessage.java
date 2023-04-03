package com.productdock.adapter.out.kafka.messages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@ToString
@Data
public class BookRatingMessage implements Serializable {

    private Long bookId;
    private Double rating;
    private int ratingsCount;
}
