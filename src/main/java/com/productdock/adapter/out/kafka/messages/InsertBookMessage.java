package com.productdock.adapter.out.kafka.messages;


import lombok.*;

import java.io.Serializable;
import java.util.List;

@Builder
@AllArgsConstructor
@ToString
@Data
public class InsertBookMessage implements Serializable {
    public final Long bookId;
    public final String title;
    public final String cover;
    public final String author;
    public final List<Topic> topics;
    public final int bookCopies;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Topic implements Serializable {

        private String id;
        private String name;
    }
}
