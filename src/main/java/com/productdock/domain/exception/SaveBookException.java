package com.productdock.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class SaveBookException extends RuntimeException {

    public SaveBookException(String message) {
        super(message);
    }

}

