package com.example.havruta.errorAndException;

import lombok.Getter;

@Getter
public class NoCategoryException extends RuntimeException{
    private ErrorCode errorCode;

    public NoCategoryException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
