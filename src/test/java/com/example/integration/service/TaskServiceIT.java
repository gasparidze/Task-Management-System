package com.example.integration.service;

import com.example.domain.dto.TaskCreateEditDto;
import com.example.domain.dto.TaskReadDto;
import com.example.domain.dto.UserReadDto;
import com.example.domain.entity.Task;
import com.example.domain.enums.Priority;
import com.example.domain.enums.Status;
import com.example.integration.IntegrationTestBase;
import com.example.repository.TaskRepository;
import com.example.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Интеграционный тест для TaskService
 */
@RequiredArgsConstructor
public class TaskServiceIT extends IntegrationTestBase {
    private static final Long AUTHOR_ID = 1L;
    private static final Long PERFORMER_ID = 2L;
    private static final String AUTHOR_EMAIL = "author@mail.ru";
    private static final String PERFORMER_EMAIL = "performer@mail.ru";
    private static final String HEADER = "testHeader";
    private static final String DESCRIPTION = "testDescription";
    private static final LocalDate CREATED_DATE = LocalDate.now();

    private final TaskRepository repository;
    private final TaskService taskService;

    @Test
    void createTask() {
        TaskCreateEditDto taskCreateEditDto = getTaskCreateEditDto();
        TaskReadDto taskReadDto = getTaskReadDto();

        TaskReadDto actualTaskDto = taskService.createTask(AUTHOR_EMAIL, taskCreateEditDto);

        assertThat(taskReadDto).isEqualTo(actualTaskDto);
    }

    @Test
    void changeTask() {
        TaskCreateEditDto taskCreateEditDto = getTaskCreateEditDto();
        taskCreateEditDto.setHeader("newHeader");
        taskCreateEditDto.setDescription("newDescription");
        taskCreateEditDto.setStatus(Status.COMPLETED.name());
        taskCreateEditDto.setPriority(Priority.HIGH.name());

        taskService.changeTask(1L, taskCreateEditDto);

        Task updatedTask = repository.findById(1L).get();

        assertThat("newHeader").isEqualTo(updatedTask.getHeader());
        assertThat("newDescription").isEqualTo(updatedTask.getDescription());
        assertThat(Status.COMPLETED).isEqualTo(updatedTask.getStatus());
        assertThat(Priority.HIGH).isEqualTo(updatedTask.getPriority());
    }

    @Test
    void removeTask() {
        taskService.removeTask(AUTHOR_ID);

        assertThat(repository.findById(AUTHOR_ID)).isNotPresent();
    }

    @Test
    void getAllTasks(){
        assertThat(taskService.getAllTasks(0, 1)).size().isEqualTo(1);
    }

    @Test
    void getTasksByAuthorId(){
        Slice<TaskReadDto> tasksByAuthorId = taskService.getTasksByAuthorId(AUTHOR_ID, 0, 1);

        assertThat(tasksByAuthorId).size().isEqualTo(1);
    }

    @Test
    void getTasksByPerformerId(){
        Slice<TaskReadDto> tasksByPerformerId = taskService.getTasksByPerformerId(PERFORMER_ID, 0, 1);

        assertThat(tasksByPerformerId).size().isEqualTo(1);
    }
    private TaskReadDto getTaskReadDto() {
        return TaskReadDto.builder()
                .id(2L)
                .header(HEADER)
                .description(DESCRIPTION)
                .status(Status.PENDING.name())
                .priority(Priority.LOW.name())
                .createdDate(CREATED_DATE)
                .author(getUserDto(AUTHOR_ID, AUTHOR_EMAIL, "author"))
                .performer(getUserDto(PERFORMER_ID, PERFORMER_EMAIL, "performer"))
                .comments(new ArrayList<>())
                .build();
    }

    private UserReadDto getUserDto(long id, String mail, String author) {
        return UserReadDto.builder()
                .id(id)
                .email(mail)
                .name(author)
                .build();
    }

    private TaskCreateEditDto getTaskCreateEditDto() {
        return TaskCreateEditDto.builder()
                .header(HEADER)
                .description(DESCRIPTION)
                .status(Status.PENDING.name())
                .priority(Priority.LOW.name())
                .performerId(PERFORMER_ID)
                .build();
    }
}
