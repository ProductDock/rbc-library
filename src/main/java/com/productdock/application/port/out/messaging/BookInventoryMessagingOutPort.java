package com.productdock.application.port.out.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.concurrent.ExecutionException;

public interface BookInventoryMessagingOutPort {

    void sendMessage(Long bookId, int bookCopies) throws ExecutionException, InterruptedException, JsonProcessingException;
}
