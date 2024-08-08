package com.example.service;

import com.example.domain.dto.*;
import com.example.http.exception.CommentException;
import com.example.mapper.CommentMapper;
import com.example.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * Сервис, отвечающий за обработку комментариев в системе
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private static final String COMMENT_NOT_FOUND = "Комментарий по данному id не найден";
    private final CommentMapper mapper;
    private final CommentRepository repository;
    private final TaskService taskService;

    public CommentReadDto createComment(CommentCreateDto commentDto) {
        taskService.checkTaskExistence(commentDto.getTaskId());

        return Optional.of(commentDto)
                .map(mapper::dtoToObject)
                .map(repository::save)
                .map(mapper::objectToDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    public void removeComment(Long id) {
        repository.findById(id)
                .ifPresentOrElse(repository::delete, () -> {
                    throw new CommentException(COMMENT_NOT_FOUND);
                });
    }
}
