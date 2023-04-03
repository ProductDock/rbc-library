package com.productdock.adapter.in.web.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddBookDto extends BaseBookDto {

    @Singular
    @NotNull
    public List<TopicDto> topics;
    @Min(1)
    @NotNull
    public Integer bookCopies;

    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Builder
    public static class TopicDto {

        @NotNull
        public Long id;
    }
}
