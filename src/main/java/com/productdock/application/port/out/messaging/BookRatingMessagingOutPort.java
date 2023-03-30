package com.productdock.application.port.out.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.productdock.domain.Book;

import java.util.concurrent.ExecutionException;

public interface BookRatingMessagingOutPort {

    void sendMessage(Book book) throws ExecutionException, InterruptedException, JsonProcessingException;

}
