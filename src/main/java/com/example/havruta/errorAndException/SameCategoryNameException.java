package com.example.havruta.errorAndException;

import lombok.Getter;

@Getter
public class SameCategoryNameException extends RuntimeException{
    private ErrorCode errorCode;

    public SameCategoryNameException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
