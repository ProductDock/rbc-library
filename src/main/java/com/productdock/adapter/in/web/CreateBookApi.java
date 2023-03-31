package com.productdock.adapter.in.web;

import com.productdock.adapter.in.web.dto.InsertBookDto;
import com.productdock.adapter.in.web.mapper.InsertBookDtoMapper;
import com.productdock.application.port.in.SaveBookUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/catalog/books")
record CreateBookApi(SaveBookUseCase saveBookUseCase, InsertBookDtoMapper bookDtoMapper) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createBook(@Valid @RequestBody InsertBookDto insertBookDto) {
        log.debug("POST request received - api/catalog/books, Payload: {}", insertBookDto);
        var book = bookDtoMapper.toDomain(insertBookDto);
        return saveBookUseCase.saveBook(book, insertBookDto.bookCopies);
    }
}
