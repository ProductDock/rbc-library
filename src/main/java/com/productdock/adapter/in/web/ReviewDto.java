package com.productdock.adapter.in.web;

import com.productdock.adapter.in.web.mapper.Recommendation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    public Long bookId;

    public String userId;

    public String userFullName;

    @Size(max = 500, message = "Comment cannot be longer than 500 characters")
    public String comment;

    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    public Short rating;

    public List<Recommendation> recommendation;

}
