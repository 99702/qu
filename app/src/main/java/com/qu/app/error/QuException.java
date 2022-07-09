package com.qu.app.error;

import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ControllerAdvice
//@ResponseStatus
//@NoArgsConstructor
public class QuException extends RuntimeException {
    public QuException(String exMessage, Exception exception){
        super(exMessage, exception);
    }
    public QuException(String exMessage){
        super(exMessage);
    }
}
