package com.example.http.exception;

/**
 * Exception, отвечающий за ошибки при обработке задач
 */
public class TaskException extends RuntimeException{
    public TaskException(String message) {
        super(message);
    }
}
