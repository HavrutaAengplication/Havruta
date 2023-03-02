package com.example.havruta.errorAndException;

import lombok.Getter;

@Getter
public class RootDeleteException extends RuntimeException{
    private ErrorCode errorCode;

    public RootDeleteException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
