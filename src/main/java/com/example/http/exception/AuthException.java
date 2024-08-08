package com.example.http.exception;

/**
 * Exception, отвечающий за ошибки при аутентификации
 */
public class AuthException extends RuntimeException{
    public AuthException(String message) {
        super(message);
    }
}
