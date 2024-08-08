package com.example.http.exception;

/**
 * Exception, отвечающий за ошибки при обработке комментариев
 */
public class CommentException extends RuntimeException{
    public CommentException(String message) {
        super(message);
    }
}
