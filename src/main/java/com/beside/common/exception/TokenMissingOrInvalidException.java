package com.beside.common.exception;

public class TokenMissingOrInvalidException extends RuntimeException {
    public TokenMissingOrInvalidException(String message) {
        super(message);
    }
}
