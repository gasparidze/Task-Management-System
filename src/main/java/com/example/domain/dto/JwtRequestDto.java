package com.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

@Schema(description = "DTO для запроса на создание токена")
@Value
public class JwtRequestDto {
    @Schema(description = "email пользователя")
    @NotBlank(message = "login must not be empty")
    String email;

    @Schema(description = "пароль пользователя")
    @NotBlank(message = "password must not be empty")
    String password;
}
