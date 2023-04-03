package com.productdock.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class GetBookDto extends BaseBookDto {

    public Long id;
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
