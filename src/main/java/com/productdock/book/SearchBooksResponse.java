package com.productdock.book;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SearchBooksResponse {

    @NonNull
    public long count;

    @NonNull
    public List<BookDto> books;
}
