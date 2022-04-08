package com.productdock.book;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "book")
public class BookEntity {

    @Id
    private String id;

    @Column(nullable=false)
    private String title;

    @Column(nullable=false)
    private String author;

    private String cover;
}
