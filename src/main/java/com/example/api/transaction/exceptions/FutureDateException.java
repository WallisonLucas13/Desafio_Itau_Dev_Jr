package com.example.api.transaction.exceptions;

public class FutureDateException extends RuntimeException {
    public FutureDateException(String message) {
        super(message);
    }
}
