package com.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "DTO для вывода задач пользователю")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskReadDto {
    @Schema(description = "id")
    private Long id;

    @Schema(description = "заголовок")
    private String header;

    @Schema(description = "описание")
    private String description;

    @Schema(description = "статус задачи")
    private String status;

    @Schema(description = "приоритет задачи")
    private String priority;

    @Schema(description = "дата/время создания задачи")
    private LocalDate createdDate;

    @Schema(description = "автор (создатель) задачи")
    private UserReadDto author;

    @Schema(description = "исполнитель задачи")
    private UserReadDto performer;

    @Schema(description = "комментарии к задаче")
    private List<CommentReadDto> comments;
}
