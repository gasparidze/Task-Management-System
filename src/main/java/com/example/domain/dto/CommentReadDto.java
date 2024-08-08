package com.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Schema(description = "DTO для вывода комментария пользователю")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentReadDto {
    @Schema(description = "id комментария")
    private Long id;

    @Schema(description = "текст комментария")
    private String text;

    @Schema(description = "дата/время создания комментария")
    private LocalDate createdDate;
}
