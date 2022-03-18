package com.productdock.model;

import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer memberId;

    @NotNull
    @Size(min=2)
    @Column(nullable=false)
    private String firstName;

    @NotNull
    @Size(min=2)
    @Column(nullable=false)
    private String lastName;

}
