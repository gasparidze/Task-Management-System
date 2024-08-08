package com.example.service;

import com.example.domain.dto.TaskCreateEditDto;
import com.example.domain.dto.TaskReadDto;
import com.example.domain.dto.UserReadDto;
import com.example.domain.entity.Task;
import com.example.domain.entity.User;
import com.example.domain.enums.Priority;
import com.example.domain.enums.Status;
import com.example.http.exception.TaskException;
import com.example.mapper.TaskMapper;
import com.example.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Класс для Unit тестирования TaskService
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    private static final Long AUTHOR_ID = 1L;
    private static final Long PERFORMER_ID = 2L;
    private static final String AUTHOR_EMAIL = "author@mail.ru";
    private static final String PERFORMER_EMAIL = "performer@mail.ru";
    private static final String PASSWORD = "123";
    private static final String HEADER = "testHeader";
    private static final String DESCRIPTION = "testDescription";
    private static final LocalDate CREATED_DATE = LocalDate.now();
    private static final String TASK_NOT_FOUND = "Задач по данному id не найдено";
    @Mock
    private TaskRepository repository;
    @Mock
    private TaskMapper mapper;
    @Mock
    private UserService userService;
    @InjectMocks
    private TaskService taskService;

    @Test
    void createTask() {
        TaskCreateEditDto taskCreateEditDto = getTaskCreateEditDto();
        User author = getUser(AUTHOR_ID, AUTHOR_EMAIL, PASSWORD, "author");
        Task taskFromDto = getTaskFromDto();
        Task task = getTask();
        TaskReadDto taskReadDto = getTaskReadDto();

        doReturn(author)
                .when(userService).getUserByEmail(AUTHOR_EMAIL);
        doReturn(taskFromDto)
                .when(mapper).dtoToObject(taskCreateEditDto);
        doReturn(task)
                .when(repository).save(taskFromDto);
        doReturn(taskReadDto)
                .when(mapper).objectToDto(task);


        TaskReadDto actualDto = taskService.createTask(AUTHOR_EMAIL, taskCreateEditDto);

        assertThat(taskReadDto).isEqualTo(actualDto);
        verify(repository).save(taskFromDto);
    }

    @Test
    void changeTask() {
        Task task = getTask();
        TaskCreateEditDto taskCreateEditDto = getTaskCreateEditDto();
        User performer = getUser(PERFORMER_ID, PERFORMER_EMAIL, PASSWORD, "performer");

        doReturn(performer).when(userService).getUserById(PERFORMER_ID);
        doReturn(Optional.of(task)).when(repository).findById(AUTHOR_ID);

        taskService.changeTask(AUTHOR_ID, taskCreateEditDto);

        verify(repository).save(task);
    }

    @Test
    void changeTaskExceptionScenario() {
        TaskCreateEditDto taskCreateEditDto = getTaskCreateEditDto();
        doReturn(Optional.empty()).when(repository).findById(Mockito.anyLong());

        TaskException taskException = assertThrows(
                TaskException.class,
                () -> taskService.changeTask(Mockito.anyLong(), taskCreateEditDto)
        );

        assertThat(taskException.getMessage()).isEqualTo(TASK_NOT_FOUND);
    }

    @Test
    void removeTask() {
        Task task = getTask();
        doReturn(Optional.of(task)).when(repository).findById(AUTHOR_ID);

        taskService.removeTask(AUTHOR_ID);

        verify(repository).delete(task);
    }

    @Test
    void removeTaskExceptionScenario() {
        doReturn(Optional.empty()).when(repository).findById(Mockito.anyLong());

        TaskException taskException = assertThrows(
                TaskException.class,
                () -> taskService.removeTask(Mockito.anyLong())
        );

        assertThat(taskException.getMessage()).isEqualTo(TASK_NOT_FOUND);
    }

    @Test
    void getAllTasks() {
        Pageable pageable = PageRequest.of(0, 1);
        Task task = getTask();
        Slice<Task> tasks = new PageImpl<>(List.of(task), pageable, 1);
        TaskReadDto taskReadDto = getTaskReadDto();

        doReturn(tasks).when(repository).findAll(pageable);
        doReturn(taskReadDto).when(mapper).objectToDto(task);

        Slice<TaskReadDto> allTasks = taskService.getAllTasks(0, 1);

        assertThat(allTasks).size().isEqualTo(1);
        verify(repository).findAll(pageable);
    }

    @Test
    void getAllTasksExceptionScenario() {
        Pageable pageable = PageRequest.of(0, 1);
        Slice<Task> tasks = new PageImpl<>(List.of(), pageable, 1);

        doReturn(tasks).when(repository).findAll(pageable);

        TaskException taskException = assertThrows(
                TaskException.class,
                () -> taskService.getAllTasks(0, 1)
        );

        assertThat(taskException.getMessage()).isEqualTo("Список задач пуст");
        verifyNoInteractions(mapper);
    }

    @Test
    void getTaskById() {
        Task task = getTask();
        TaskReadDto taskReadDto = getTaskReadDto();

        doReturn(Optional.of(task)).when(repository).findById(AUTHOR_ID);
        doReturn(taskReadDto).when(mapper).objectToDto(task);

        TaskReadDto actualTask = taskService.getTaskById(AUTHOR_ID);

        assertThat(taskReadDto).isEqualTo(actualTask);
        verify(repository).findById(AUTHOR_ID);
    }

    @Test
    void checkTaskExistence(){
        doReturn(Optional.empty()).when(repository).findById(Mockito.anyLong());

        TaskException taskException = assertThrows(
                TaskException.class,
                () -> taskService.checkTaskExistence(Mockito.anyLong())
        );

        assertThat(TASK_NOT_FOUND).isEqualTo(taskException.getMessage());
    }

    @Test
    void getTasksByAuthorId() {
        getTasksByCondition(AUTHOR_ID);
    }

    @Test
    void getTasksByPerformerId() {
        getTasksByCondition(PERFORMER_ID);
    }

    private void getTasksByCondition(Long id){
        Pageable pageable = PageRequest.of(0, 1);
        Task task = getTask();
        TaskReadDto taskReadDto = getTaskReadDto();
        Slice<Task> tasks = new PageImpl<>(List.of(task), pageable, 1);

        doReturn(taskReadDto).when(mapper).objectToDto(task);
        Slice<TaskReadDto> tasksById;
        if(id == AUTHOR_ID) {
            doReturn(tasks).when(repository).findAllByAuthorId(id, pageable);
            tasksById = taskService.getTasksByAuthorId(id, 0, 1);
        } else{
            doReturn(tasks).when(repository).findAllByPerformerId(id, pageable);
            tasksById = taskService.getTasksByPerformerId(id, 0, 1);
        }

        assertThat(tasksById).size().isEqualTo(1);
        if(id == AUTHOR_ID){
            verify(repository).findAllByAuthorId(AUTHOR_ID, pageable);
        } else {
            verify(repository).findAllByPerformerId(PERFORMER_ID, pageable);
        }
    }

    private TaskReadDto getTaskReadDto() {
        return TaskReadDto.builder()
                .id(AUTHOR_ID)
                .header(HEADER)
                .description(DESCRIPTION)
                .status(Status.PENDING.name())
                .priority(Priority.LOW.name())
                .createdDate(CREATED_DATE)
                .author(getUserDto(AUTHOR_ID, AUTHOR_EMAIL, "author"))
                .performer(getUserDto(PERFORMER_ID, PERFORMER_EMAIL, "performer"))
                .build();
    }

    private Task getTask() {
        return Task.builder()
                .id(AUTHOR_ID)
                .header(HEADER)
                .description(DESCRIPTION)
                .status(Status.PENDING)
                .priority(Priority.LOW)
                .createdDate(CREATED_DATE)
                .author(getUser(AUTHOR_ID, AUTHOR_EMAIL, PASSWORD, "author"))
                .performer(getUser(PERFORMER_ID, PERFORMER_EMAIL, PASSWORD, "performer"))
                .build();
    }

    private Task getTaskFromDto() {
        return Task.builder()
                .header(HEADER)
                .description(DESCRIPTION)
                .status(Status.PENDING)
                .priority(Priority.LOW)
                .performer(getUser(PERFORMER_ID, PERFORMER_EMAIL, PASSWORD, "performer"))
                .build();
    }

    private User getUser(long id, String email, String password, String name) {
        return User.builder()
                .id(id)
                .email(email)
                .password(password)
                .name(name)
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