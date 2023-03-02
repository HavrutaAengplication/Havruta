package com.example.havruta.errorAndException;

import lombok.Getter;

@Getter
public class NoUserException  extends RuntimeException{
    private ErrorCode errorCode;

    public NoUserException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }
}
