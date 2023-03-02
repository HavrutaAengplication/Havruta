package com.example.havruta.errorAndException;

import lombok.Getter;

@Getter
public class NoProblemException extends RuntimeException{
    private ErrorCode errorCode;

    public NoProblemException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
