package com.productdock.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.Serializable;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Review {

    @EmbeddedId
    private ReviewCompositeKey reviewCompositeKey;

    @Column(nullable=false)
    private String userFullName;

    @Size(max = 500)
    private String comment;

    @Min(1)
    @Max(5)
    private Short rating;

    private Integer recommendation;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class ReviewCompositeKey implements Serializable {

        protected Long bookId;
        protected String userId;

    }

}
