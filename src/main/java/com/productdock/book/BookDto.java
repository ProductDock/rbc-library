package com.productdock.book;

import lombok.AllArgsConstructor;

import java.util.List;

public class BookDto {

    public Long id;
    public String title;
    public String author;
    public String cover;
    public String description;
    public List<String> topics;
    public List<ReviewDto> reviews;
    public RatingDto rating;

    public static class ReviewDto {

        public String userFullName;
        public Short rating;
        public List<Recommendation> recommendation;
        public String comment;
    }

    @AllArgsConstructor
    public static class RatingDto {
        public Double score;
        public Integer count;
    }
}
