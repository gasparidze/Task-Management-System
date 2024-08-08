package com.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO для создания пользователя")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateDto {
    @Schema(description = "email пользователя")
    @Email(message = "email isn't valid")
    private String email;

    @Schema(description = "пароль пользователя")
    @NotBlank(message = "password must not be empty")
    private String password;

    @Schema(description = "имя пользователя")
    @NotBlank(message = "name must not be empty")
    private String name;
}
