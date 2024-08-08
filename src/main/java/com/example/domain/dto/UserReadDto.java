package com.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "DTO для вывода пользователя")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserReadDto {
    @Schema(description = "id пользователя")
    private Long id;

    @Schema(description = "email пользователя")
    private String email;

    @Schema(description = "имя пользователя")
    private String name;
}
