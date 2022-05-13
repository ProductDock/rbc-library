package com.productdock.book;

import java.io.Serializable;
import java.util.List;

public class BookDto implements Serializable {

    public Long id;
    public String title;
    public String author;
    public String cover;
    public List<ReviewDto> reviews;

    public static class ReviewDto implements Serializable {

        public String userFullName;
        public Short rating;
        public List<Recommendation> recommendation;
        public String comment;
    }
}
