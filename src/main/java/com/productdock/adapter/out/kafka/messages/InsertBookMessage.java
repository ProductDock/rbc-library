package com.productdock.adapter.out.kafka.messages;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@ToString
public class InsertBookMessage implements Serializable {
    private String bookId;
    private String title;
    private String cover;
    private String author;
//    @Singular
//    private List<Topic> topics;
    private int bookCopies;
}
