package com.example.service;

import com.example.domain.dto.TaskCreateEditDto;
import com.example.domain.dto.TaskReadDto;
import com.example.domain.entity.Task;
import com.example.domain.entity.User;
import com.example.domain.enums.Priority;
import com.example.domain.enums.Status;
import com.example.http.exception.TaskException;
import com.example.mapper.TaskMapper;
import com.example.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.function.BiFunction;

/**
 * Сервис, отвечающий за обработку задач в системе
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {
    private static final String TASK_NOT_FOUND = "Задач по данному id не найдено";
    private static final String TASK_LIST_EMPTY = "Список задач пуст";
    private final TaskMapper mapper;
    private final TaskRepository repository;
    private final UserService userService;

    @Transactional
    public TaskReadDto createTask(String authorEmail, TaskCreateEditDto taskDto) {
        User author = userService.getUserByEmail(authorEmail);
        userService.checkUserExistence(taskDto.getPerformerId());

        return Optional.of(taskDto)
                .map(mapper::dtoToObject)
                .map(task -> {
                    task.setAuthor(author);
                    return task;
                })
                .map(repository::save)
                .map(mapper::objectToDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Transactional
    public void changeTask(Long taskId, TaskCreateEditDto taskDto) {
        Task foundTask = repository.findById(taskId)
                .orElseThrow(() -> new TaskException(TASK_NOT_FOUND));

        User performer = userService.getUserById(taskDto.getPerformerId());

        Task task = foundTask;
        task.setHeader(taskDto.getHeader());
        task.setDescription(taskDto.getDescription());
        task.setStatus(Status.valueOf(taskDto.getStatus()));
        task.setPriority(Priority.valueOf(taskDto.getPriority()));
        task.setPerformer(performer);
        repository.save(task);
    }

    @Transactional
    public void removeTask(Long taskId) {
        repository.findById(taskId)
                .ifPresentOrElse(repository::delete, () -> {
                    throw new TaskException(TASK_NOT_FOUND);
                });
    }

    public Slice<TaskReadDto> getAllTasks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Task> foundTasks = repository.findAll(pageable);
        doesTasksExist(foundTasks);

        return foundTasks.map(mapper::objectToDto);
    }

    public TaskReadDto getTaskById(Long taskId) {
        return repository.findById(taskId)
                .map(mapper::objectToDto)
                .orElseThrow(() -> new TaskException(TASK_NOT_FOUND));
    }

    public Slice<TaskReadDto> getTasksByAuthorId(Long authorId, int page, int size) {
        return getTasksByCondition(authorId, page, size, repository::findAllByAuthorId);
    }

    public Slice<TaskReadDto> getTasksByPerformerId(Long performerId, int page, int size) {
        return getTasksByCondition(performerId, page, size, repository::findAllByPerformerId);
    }

    private Slice<TaskReadDto> getTasksByCondition(Long performerId,
                                                   int page, int size,
                                                   BiFunction<Long, Pageable, Slice<Task>> func) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Task> foundTasks = func.apply(performerId, pageable);
        doesTasksExist(foundTasks);

        return foundTasks.map(mapper::objectToDto);
    }

    public void checkTaskExistence(Long id){
        if(!repository.findById(id).isPresent()){
            throw new TaskException(TASK_NOT_FOUND);
        }
    }

    private void doesTasksExist(Slice<Task> tasks) {
        if (tasks.isEmpty()) {
            throw new TaskException(TASK_LIST_EMPTY);
        }
    }
}
