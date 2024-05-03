package com.example.testassignment.exceptions;

import com.example.testassignment.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserGlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorDto> dataNotFoundHandler(
            NoSuchUserException exception) {
        ErrorDto incorrectData = new ErrorDto(exception.getMessage());
        return new ResponseEntity<>(incorrectData, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDto> validationErrorsHandler(
            InvalidDataException exception) {
        ErrorDto incorrectData = new ErrorDto(exception.getMessage());
        return new ResponseEntity<>(incorrectData, HttpStatus.BAD_REQUEST);
    }
}