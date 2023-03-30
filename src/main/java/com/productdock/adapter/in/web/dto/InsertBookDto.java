package com.productdock.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsertBookDto {
    public String title;
    public String author;
    public String cover;
    public String description;
    public List<BookDto.TopicDto> topics;
    public Integer bookCopies;

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TopicDto {
        public Long id;
        public String name;
    }
}
