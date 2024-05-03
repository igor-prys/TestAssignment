package com.example.testassignment.exceptions;

import lombok.Data;

@Data
public class NoSuchUserException extends RuntimeException {

    public NoSuchUserException() {
        super();
    }

    public NoSuchUserException(String message) {
        super(message);
    }
}
