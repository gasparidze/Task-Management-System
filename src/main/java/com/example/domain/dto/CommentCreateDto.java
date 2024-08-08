package com.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO для создания комментария")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentCreateDto {
    @Schema(description = "текст комментария")
    @NotBlank(message = "text must not be empty")
    private String text;

    @Schema(description = "id задачи")
    @NotNull(message = "taskId must not be null")
    @Positive(message = "taskId must be positive")
    private Long taskId;
}
