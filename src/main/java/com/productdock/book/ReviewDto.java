package com.productdock.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto implements Serializable {

    public Long bookId;
    public String userId;
    public String userFullName;
    public String comment;
    public Short rating;
    public List<Recommendation> recommendation;

}
