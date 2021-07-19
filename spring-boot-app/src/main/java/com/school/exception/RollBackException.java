package com.school.exception;

public class RollBackException extends Exception {
    public RollBackException(String errorMessage) {
        super(errorMessage);
    }
}