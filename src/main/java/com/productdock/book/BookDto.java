package com.productdock.book;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class BookDto implements Serializable {

    private Long id;

    private String title;

    private String author;

    private String cover;

}
