package com.productdock.book;

import java.util.List;

public class BookDto {

    public Long id;
    public String title;
    public String author;
    public String cover;
    public List<ReviewDto> reviews;

    public static class ReviewDto {

        public String userFullName;
        public Short rating;
        public List<Recommendation> recommendation;
        public String comment;
    }
}
