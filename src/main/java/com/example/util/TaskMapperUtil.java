package com.example.util;

import com.example.domain.entity.Task;
import com.example.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

/**
 * util-класс для работы с CommentMapper
 */
@Named("TaskMapperUtil")
@Component
@RequiredArgsConstructor
public class TaskMapperUtil {
    private final TaskRepository repository;
    @Named("getTask")
    public Task getTask(Long id) {
        return repository.findById(id)
                .orElseThrow();
    }
}
