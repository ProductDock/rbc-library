package com.productdock.application.port.out.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.concurrent.ExecutionException;

public interface BookMessagingOutPort {

    void sendMessage(String topic, Object message) throws ExecutionException, InterruptedException, JsonProcessingException;

}
