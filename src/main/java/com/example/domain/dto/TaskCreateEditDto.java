package com.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Schema(description = "DTO для создания/редактирования задачи")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskCreateEditDto {
    @Schema(description = "заголовок")
    @NotBlank(message = "header must not be empty")
    private String header;

    @Schema(description = "описание")
    @NotBlank(message = "description must not be empty")
    private String description;

    @Schema(description = "статус задачи", example = "PENDING/PROGRESS/COMPLETED")
    @NotEmpty
    @NotBlank
    @Pattern(regexp = "(PENDING|PROGRESS|COMPLETED)",
            message = "status must match PENDING/PROGRESS/COMPLETED")
    private String status;

    @Schema(description = "приоритет задачи", example = "LOW/MIDDLE/HIGH")
    @NotNull
    @NotBlank
    @Pattern(regexp = "(LOW|MIDDLE|HIGH)",
            message = "priority must match LOW/MIDDLE/HIGH")
    private String priority;

    @Schema(description = "id исполнителя задачи")
    @NotNull(message = "performerId must not be null")
    @Positive(message = "performerId must be positive")
    private Long performerId;
}
