package com.example.http.handler;

import com.example.http.exception.AuthException;
import com.example.domain.dto.ExceptionResponseDto;
import com.example.http.exception.CommentException;
import com.example.http.exception.TaskException;
import com.example.http.exception.UserException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * Обработчик исключений
 */
@RestControllerAdvice(basePackages = "com.example.http.controller")
public class RestControllerExceptionHandler {
    /**
     * метод, обрабатывающий исключения, выбрасывающиеся валидационными аннотациямм
     * @param ex - exception
     * @return ResponseEntity - ответ
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleException(MethodArgumentNotValidException ex){
        List<String> errors = new ArrayList<>();
        ex.getAllErrors().forEach(el -> errors.add(el.getDefaultMessage()));

        return ResponseEntity.badRequest().body(new ExceptionResponseDto(errors));
    }

    /**
     * метод, обрабатывающий исключения, выбрасывающиеся при:
     * 1) валидации параметров методов контроллеров (ConstraintViolationException)
     * 2) процессе взаимодействия с задачей (TaskException)
     * 3) процессе аутентификации (AuthException)
     * 4) остальных ошибках
     * @param ex - exception
     * @return ResponseEntity - ответ
     */
    @ExceptionHandler(value = {ConstraintViolationException.class,
            TaskException.class, AuthException.class, RuntimeException.class})
    public ResponseEntity<?> handleException(Exception ex){
        ResponseEntity<ExceptionResponseDto> response;

        if(ex instanceof ConstraintViolationException || ex instanceof HttpMessageNotReadableException){
            response = ResponseEntity.badRequest().body(
                    new ExceptionResponseDto(ex.getMessage())
            );
        } else if (ex instanceof TaskException || ex instanceof UserException
                || ex instanceof CommentException) {
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ExceptionResponseDto(ex.getMessage())
            );
        } else if (ex instanceof AuthException) {
            response = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ExceptionResponseDto(ex.getMessage())
            );
        } else {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ExceptionResponseDto(ex.getMessage())
            );
        }

        return response;
    }
}
