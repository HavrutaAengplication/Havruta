package com.example.havruta.errorAndException;

import lombok.Getter;

@Getter
public class NotAdminException extends RuntimeException{
    private ErrorCode errorCode;

    public NotAdminException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
