package com.productdock.application.port.out.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.concurrent.ExecutionException;

public interface DeleteBookMessagingOutPort {
    void sendMessage(Long bookId) throws ExecutionException, InterruptedException, JsonProcessingException;

}
