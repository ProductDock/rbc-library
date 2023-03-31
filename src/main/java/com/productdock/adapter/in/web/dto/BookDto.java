package com.productdock.adapter.in.web.dto;

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
    public List<TopicDto> topics;
    public List<ReviewDto> reviews;
    public RatingDto rating;

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RatingDto {
        public Double score;
        public Integer count;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TopicDto {
        public Long id;
        public String name;
    }
}