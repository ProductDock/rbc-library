package com.productdock.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
class BaseBookDto {

    @NotNull
    public String title;
    @NotNull
    public String author;
    @NotNull
    public String cover;
    @NotNull
    public String description;
}
