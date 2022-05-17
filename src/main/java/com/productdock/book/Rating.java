package com.productdock.book;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Rating {
    private double score;
    private int count;
}
