package com.productdock.book;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "book")
public class BookEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String title;

    @Column(nullable=false)
    private String author;

    private String cover;

    @Singular
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "book_topic",
            joinColumns = { @JoinColumn(name = "book_id") },
            inverseJoinColumns = { @JoinColumn(name = "topic_id") }
    )
    private Set<TopicEntity> topics;

}
