package com.productdock.adapter.in.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    public Long id;
    public String title;
    public String author;
    public String cover;
    public String description;
    public List<String> topics;
    public List<ReviewDto> reviews;
    public RatingDto rating;

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RatingDto {
        public Double score;
        public Integer count;
    }
}
