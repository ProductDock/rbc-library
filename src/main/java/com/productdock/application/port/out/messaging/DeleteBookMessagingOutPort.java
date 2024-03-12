package com.productdock.application.port.out.messaging;

public interface DeleteBookMessagingOutPort {
    void sendMessage(Long bookId);

}
