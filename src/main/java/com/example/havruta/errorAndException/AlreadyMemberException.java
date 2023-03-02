package com.example.havruta.errorAndException;

import lombok.Getter;

@Getter
public class AlreadyMemberException extends RuntimeException{
    private ErrorCode errorCode;

    public AlreadyMemberException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
