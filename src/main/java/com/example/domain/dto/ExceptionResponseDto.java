package com.example.domain.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO для вывода ошибок пользователю
 */
@Getter
public class ExceptionResponseDto {
    /**
     * список сообщений об ошибках
     */
    private List<String> messages;

    public ExceptionResponseDto(List<String> messages) {
        this.messages = messages;
    }

    public ExceptionResponseDto(String message) {
        this.messages = new ArrayList<>();
        this.messages.add(message);
    }
}
