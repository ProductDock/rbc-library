package com.productdock.adapter.in.web;

import com.productdock.adapter.in.web.dto.InsertBookDto;
import com.productdock.adapter.in.web.mapper.InsertBookDtoMapper;
import com.productdock.application.port.in.SaveBookUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/catalog/books")
record CreateBookApi(SaveBookUseCase saveBookUseCase, InsertBookDtoMapper bookDtoMapper) {

    @PostMapping
    public void createBook(@Valid @RequestBody InsertBookDto insertBookDto) {
        log.debug("POST request received - api/catalog/books, Payload: {}", insertBookDto);
        var book = bookDtoMapper.toDomain(insertBookDto);
        saveBookUseCase.saveBook(book, insertBookDto.bookCopies);
    }
}
