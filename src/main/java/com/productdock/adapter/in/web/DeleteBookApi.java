package com.productdock.adapter.in.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/catalog/books")
public record DeleteBookApi() {

    @DeleteMapping("/{bookId}")
    public void DeleteBook(@PathVariable("bookId") Long bookId){
        log.info("DELETE request recieved");
    }
}
