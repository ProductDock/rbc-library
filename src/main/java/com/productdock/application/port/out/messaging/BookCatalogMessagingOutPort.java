package com.productdock.application.port.out.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.productdock.domain.Book;

import java.util.concurrent.ExecutionException;

public interface BookMessagingOutPort {

    void sendMessage(Book book, int bookCopies) throws ExecutionException, InterruptedException, JsonProcessingException;
}
