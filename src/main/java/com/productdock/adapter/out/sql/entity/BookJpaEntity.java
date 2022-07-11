package com.productdock.adapter.out.sql.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "book")
public class BookJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    private String cover;

    private String description;

    @Singular
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "book_topic",
            joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn(name = "topic_id")}
    )
    private Set<TopicJpaEntity> topics;

    @OneToMany(fetch = FetchType.EAGER)
    @Singular
    @JoinColumn(name = "bookId")
    @OrderBy("date DESC")
    private List<ReviewJpaEntity> reviews;
}
