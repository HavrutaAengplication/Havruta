package com.example.havruta.errorAndException;

import lombok.Getter;

@Getter
public class NotMemberException  extends RuntimeException{
    private ErrorCode errorCode;

    public NotMemberException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}