package com.example.http.controller;

import com.example.domain.dto.*;
import com.example.service.CommentService;
import com.example.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name = "TaskController", description = "Контроллер для взаимодействия с сервисом по обработке задач")
@RestController
@Validated
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Slf4j
public class TaskRestController {
    private final TaskService taskService;
    private final CommentService commentService;

    @Operation(
            summary = "Создание задачи",
            description = "Позволяет создать новую задачу в системе"
    )
    @PostMapping
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskReadDto createTask(Principal principal, @Valid @RequestBody TaskCreateEditDto taskDto){
        return taskService.createTask(principal.getName(), taskDto);
    }

    @Operation(
            summary = "Редактирование задачи",
            description = "Позволяет изменять существующую задачу по ее id"
    )
    @PatchMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.OK)
    public void changeTask(@PathVariable("id") Long taskId,
                           @Valid @RequestBody TaskCreateEditDto taskDto){

        taskService.changeTask(taskId, taskDto);
    }

    @Operation(
            summary = "Удаление задачи",
            description = "Позволяет удалять задачу по ее id"
    )
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeTask(@PathVariable("id") Long taskId){
        taskService.removeTask(taskId);
    }

    @Operation(
            summary = "Поиск всех задач",
            description = "Позволяет найти все существующие задачи в системе"
    )
    @GetMapping
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.OK)
    public Slice<TaskReadDto> getAllTasks(@NotNull @RequestParam(value = "offset", defaultValue = "0") int page,
                                   @NotNull @RequestParam(value = "limit") int size){
        return taskService.getAllTasks(page, size);
    }

    @Operation(
            summary = "Поиск задачи",
            description = "Позволяет найти задачу по ее id"
    )
    @GetMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.OK)
    @Cacheable(value = "tasks")
    public TaskReadDto getTaskById(@PathVariable("id") Long taskId){
        return taskService.getTaskById(taskId);
    }

    @Operation(
            summary = "Поиск задач автора",
            description = "Позволяет найти все задачи, созданные автором по его id"
    )
    @GetMapping("/author/{id}")
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.OK)
    public Slice<TaskReadDto> getTasksByAuthorId(@PathVariable("id") Long authorId,
                                                @NotNull @RequestParam(value = "offset", defaultValue = "0") int page,
                                                @NotNull @RequestParam(value = "limit") int size){
        return taskService.getTasksByAuthorId(authorId, page, size);
    }

    @Operation(
            summary = "Поиск задач исполнителя",
            description = "Позволяет найти все задачи, назначенные на исполнителя по его id"
    )
    @GetMapping("/performer/{id}")
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.OK)
    public Slice<TaskReadDto> getTasksByPerformerId(@PathVariable("id") Long performerId,
                                                    @NotNull @RequestParam(value = "offset", defaultValue = "0") int page,
                                                    @NotNull @RequestParam(value = "limit") int size){
        return taskService.getTasksByPerformerId(performerId, page, size);
    }

    @Operation(
            summary = "Создание нового комментария",
            description = "Позволяет создать новый комментарий в системе"
    )
    @PostMapping("/comments")
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentReadDto createComment(@Valid @RequestBody CommentCreateDto commentDto){
        return commentService.createComment(commentDto);
    }

    @Operation(
            summary = "Удаление комментария",
            description = "Позволяет удалять комментарий по его id"
    )
    @DeleteMapping("/comments/{id}")
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeComment(@PathVariable("id") Long id){
        commentService.removeComment(id);
    }
}
