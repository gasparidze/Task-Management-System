package com.example.http.controller;

import com.example.domain.dto.CommentCreateDto;
import com.example.domain.dto.TaskCreateEditDto;
import com.example.domain.enums.Priority;
import com.example.domain.enums.Status;
import com.example.integration.IntegrationTestBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Тестовый класс, проверяющий TaskRestController
 */
@AutoConfigureMockMvc
@RequiredArgsConstructor
class TaskRestControllerTest extends IntegrationTestBase {
    private static final Long ID = 1L;
    private static final Long PERFORMER_ID = 2L;
    private static final String HEADER = "testHeader";
    private static final String DESCRIPTION = "testDescription";
    private static final String PERFORMER_EMAIL = "performer@mail.ru";
    private static final String COMMENT_TEXT = "testComment";

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Test
    void createTask() throws Exception {
        TaskCreateEditDto taskCreateEditDto = getTaskCreateEditDto();
        String requestBody = objectMapper.writeValueAsString(taskCreateEditDto);
        mockMvc.perform(post("/api/v1/tasks")
                        .with(user(PERFORMER_EMAIL))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void changeTask() throws Exception {
        TaskCreateEditDto taskCreateEditDto = getTaskCreateEditDto();
        String requestBody = objectMapper.writeValueAsString(taskCreateEditDto);
        mockMvc.perform(patch("/api/v1/tasks/" + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void removeTask() throws Exception {
        mockMvc.perform(delete("/api/v1/tasks/" + ID))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getAllTasks() throws Exception {
        mockMvc.perform(get("/api/v1/tasks")
                        .param("offset", "0")
                        .param("limit","1"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getTaskById() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/" + ID))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getTasksByAuthorId() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/author/" + ID)
                        .param("offset", "0")
                        .param("limit","1"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getTasksByPerformerId() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/performer/" + PERFORMER_ID)
                        .param("offset", "0")
                        .param("limit","1"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void createComment() throws Exception {
        CommentCreateDto commentCreateDto = getCommentCreateDto();
        String requestBody = objectMapper.writeValueAsString(commentCreateDto);
        mockMvc.perform(post("/api/v1/tasks/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void removeComment() throws Exception {
        mockMvc.perform(delete("/api/v1/tasks/comments/" + ID))
                .andExpect(status().is2xxSuccessful());
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

    private CommentCreateDto getCommentCreateDto() {
        return CommentCreateDto.builder()
                .taskId(ID)
                .text(COMMENT_TEXT)
                .build();
    }
}