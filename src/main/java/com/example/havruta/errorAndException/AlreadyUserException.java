package com.example.havruta.errorAndException;

import lombok.Getter;

@Getter
public class AlreadyUserException extends RuntimeException{
    private ErrorCode errorCode;

    public AlreadyUserException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
