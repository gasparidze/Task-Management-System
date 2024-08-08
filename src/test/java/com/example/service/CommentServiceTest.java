package com.example.service;

import com.example.domain.dto.CommentCreateDto;
import com.example.domain.dto.CommentReadDto;
import com.example.domain.entity.Comment;
import com.example.domain.entity.Task;
import com.example.http.exception.CommentException;
import com.example.mapper.CommentMapper;
import com.example.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Класс для Unit тестирования CommentService
 */
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    private static final String COMMENT_NOT_FOUND = "Комментарий по данному id не найден";
    private static final Long ID = 1L;
    private static final String COMMENT_TEXT = "testComment";
    private static final LocalDate CREATED_DATE = LocalDate.now();
    @Mock
    private CommentMapper mapper;
    @Mock
    private CommentRepository repository;
    @Mock
    private TaskService taskService;
    @InjectMocks
    private CommentService commentService;

    @Test
    void createComment() {
        CommentCreateDto commentCreateDto = getCommentCreateDto();
        CommentReadDto commentReadDto = getCommentReadDto();
        Comment commentFromDto = Comment.builder()
                .text(COMMENT_TEXT)
                .createdDate(CREATED_DATE)
                .task(getTask())
                .build();
        Comment comment = getComment();

        doNothing().when(taskService).checkTaskExistence(ID);
        doReturn(commentFromDto).when(mapper).dtoToObject(commentCreateDto);
        doReturn(comment).when(repository).save(commentFromDto);
        doReturn(commentReadDto).when(mapper).objectToDto(comment);

        CommentReadDto actualComment = commentService.createComment(commentCreateDto);

        assertThat(commentReadDto).isEqualTo(actualComment);
        verify(repository).save(commentFromDto);
    }

    @Test
    void removeComment() {
        Comment comment = getComment();
        doReturn(Optional.of(comment)).when(repository).findById(ID);

        commentService.removeComment(ID);

        verify(repository).delete(comment);
    }

    @Test
    void removeCommentExceptionScenario() {
        doReturn(Optional.empty()).when(repository).findById(Mockito.anyLong());

        CommentException commentException = assertThrows(
                CommentException.class,
                () -> commentService.removeComment(Mockito.anyLong())
        );

        assertThat(commentException.getMessage()).isEqualTo(COMMENT_NOT_FOUND);
    }

    private Task getTask() {
        return Task.builder()
                .id(ID)
                .header("testDescription")
                .build();
    }

    private Comment getComment() {
        return Comment.builder()
                .id(ID)
                .text(COMMENT_TEXT)
                .createdDate(CREATED_DATE)
                .task(getTask())
                .build();
    }

    private CommentReadDto getCommentReadDto() {
        return CommentReadDto.builder()
                .id(ID)
                .text(COMMENT_TEXT)
                .createdDate(CREATED_DATE)
                .build();
    }

    private CommentCreateDto getCommentCreateDto() {
        return CommentCreateDto.builder()
                .taskId(ID)
                .text(COMMENT_TEXT)
                .build();
    }
}