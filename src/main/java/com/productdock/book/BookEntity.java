package com.productdock.book;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@Table(name = "book")
public class BookEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min=2)
    @Column(nullable=false)
    private String title;

    @NotNull
    @Size(min=2)
    @Column(nullable=false)
    private String author;

    private String cover;
}
