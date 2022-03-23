package com.productdock.book;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BookDto implements Serializable {

    private Long id;

    private String title;

    private String author;

    private String cover;

}
