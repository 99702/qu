package com.qu.app.error;

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
