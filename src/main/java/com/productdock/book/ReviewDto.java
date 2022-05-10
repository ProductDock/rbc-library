package com.productdock.book;

import java.util.List;

public class ReviewDto {

    public Long bookId;
    public String userId;
    public String userFullName;
    public String comment;
    public Short rating;
    public List<Recommendation> recommendation;

}
