package com.productdock.adapter.in.web;

import com.productdock.adapter.in.web.dto.AddBookDto;
import com.productdock.adapter.in.web.mapper.AddBookDtoMapper;
import com.productdock.application.port.in.AddBookUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/catalog/books")
record AddBookApi(AddBookUseCase addBookUseCase, AddBookDtoMapper bookDtoMapper) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createBook(@Valid @RequestBody AddBookDto addBookDto) {
        log.debug("POST request received - api/catalog/books, Payload: {}", addBookDto);
        var book = bookDtoMapper.toDomain(addBookDto);
        return addBookUseCase.addBook(book, addBookDto.bookCopies);
    }
}
