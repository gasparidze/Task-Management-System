package com.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Schema(description = "DTO для отображения токена пользователю")
@Value
public class JwtResponseDto {
    @Schema(description = "тип токена")
    String type = "Bearer";

    @Schema(description = "jwt токен")
    String jwt;
}
