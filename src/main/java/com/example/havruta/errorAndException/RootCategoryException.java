package com.example.havruta.errorAndException;

import lombok.Getter;

@Getter
public class RootCategoryException extends RuntimeException{
    private ErrorCode errorCode;

    public RootCategoryException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
