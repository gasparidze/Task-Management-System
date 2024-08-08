package com.example.http.exception;

/**
 * Exception, отвечающий за ошибки при обработке пользователей
 */
public class UserException extends RuntimeException{
    public UserException(String message) {
        super(message);
    }
}
