package com.productdock.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class DeleteBookException extends RuntimeException {

    public DeleteBookException(String message){ super(message);}
}
