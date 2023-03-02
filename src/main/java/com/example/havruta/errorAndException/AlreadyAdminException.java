package com.example.havruta.errorAndException;

import lombok.Getter;

@Getter
public class AlreadyAdminException extends RuntimeException{
    private ErrorCode errorCode;

    public AlreadyAdminException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}