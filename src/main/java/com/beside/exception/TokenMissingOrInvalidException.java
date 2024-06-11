package com.beside.exception;

public class TokenMissingOrInvalidException extends RuntimeException {
    public TokenMissingOrInvalidException(String message) {
        super(message);
    }
}
