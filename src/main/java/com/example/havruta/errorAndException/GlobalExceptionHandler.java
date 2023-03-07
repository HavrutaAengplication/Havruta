package com.example.havruta.errorAndException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoGroupException.class)
    public ResponseEntity<ErrorResponse> handleNoGroupException(NoGroupException ex) {
        log.error("handleNoGroupException", ex);
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }
    @ExceptionHandler(NoUserException.class)
    public ResponseEntity<ErrorResponse> handleNoUserException(NoUserException ex) {
        log.error("handleNoUserException", ex);
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }
    @ExceptionHandler(NoCategoryException.class)
    public ResponseEntity<ErrorResponse> handleNoCategoryException(NoCategoryException ex) {
        log.error("handleNoCategoryException", ex);
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }
    @ExceptionHandler(NoProblemException.class)
    public ResponseEntity<ErrorResponse> handleNoProblemException(NoProblemException ex) {
        log.error("handleNoProblemException", ex);
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }
    @ExceptionHandler(AlreadyMemberException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyMemberException(AlreadyMemberException ex) {
        log.error("handleAlreadyMemberException", ex);
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }
    @ExceptionHandler(AlreadyAdminException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyAdminException(AlreadyAdminException ex) {
        log.error("handleAlreadyAdminException", ex);
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }
    @ExceptionHandler(NotMemberException.class)
    public ResponseEntity<ErrorResponse> handleNotMemberException(NotMemberException ex) {
        log.error("handleNotMemberException", ex);
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }
    @ExceptionHandler(NotAdminException.class)
    public ResponseEntity<ErrorResponse> handleNotAdminException(NotAdminException ex) {
        log.error("handleNotAdminException", ex);
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }
    @ExceptionHandler(SameCategoryNameException.class)
    public ResponseEntity<ErrorResponse> handleSameCategoryNameException(SameCategoryNameException ex) {
        log.error("handleSameCategoryNameException", ex);
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }
    @ExceptionHandler(RootCategoryException.class)
    public ResponseEntity<ErrorResponse> handleRootCategoryException(RootCategoryException ex) {
        log.error("handleRootCategoryException", ex);
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }
}
