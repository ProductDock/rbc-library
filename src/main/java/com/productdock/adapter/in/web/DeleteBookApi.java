package com.productdock.adapter.in.web;

import com.productdock.application.port.in.DeleteBookUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/catalog/books")
public record DeleteBookApi(DeleteBookUseCase deleteBookUseCase) {

    @DeleteMapping("/{bookId}")
    public void deleteBook(@PathVariable("bookId") Long bookId){
        log.debug("DELETE request recieved with book id: {}", bookId);
        deleteBookUseCase.deleteBook(bookId);
    }
}
