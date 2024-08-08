package com.example.service;

import com.example.domain.dto.UserCreateDto;
import com.example.domain.dto.UserReadDto;
import com.example.domain.entity.User;
import com.example.http.exception.UserException;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Класс для Unit тестирования UserService
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static final String USER_NOT_FOUND = "Пользователь (автор или исполнитель) по данному id не найден";
    private static final Long AUTHOR_ID = 1L;
    private static final String AUTHOR_EMAIL = "author@mail.ru";
    private static final String PASSWORD = "123";
    private static final String AUTHOR_NAME = "author";
    @Mock
    private UserMapper mapper;
    @Mock
    private UserRepository repository;
    @InjectMocks
    private UserService userService;

    @Test
    void createUser() {
        UserCreateDto userCreateDto = getUserCreateDto();
        UserReadDto userReadDto = getUserReadDto();
        User authorFromDto = User.builder()
                .email(AUTHOR_EMAIL)
                .password(PASSWORD)
                .name(AUTHOR_NAME)
                .build();
        User author = getUser();

        doReturn(authorFromDto).when(mapper).dtoToObject(userCreateDto);
        doReturn(author).when(repository).save(authorFromDto);
        doReturn(userReadDto).when(mapper).objectToDto(author);

        UserReadDto savedUser = userService.createUser(userCreateDto);

        assertThat(userReadDto).isEqualTo(savedUser);
        verify(repository).save(authorFromDto);
    }

    @Test
    void getUserById() {
        User user = getUser();
        doReturn(Optional.of(user)).when(repository).findById(AUTHOR_ID);

        User actualUser = userService.getUserById(AUTHOR_ID);

        assertThat(user).isEqualTo(actualUser);
        verify(repository).findById(AUTHOR_ID);
    }

    @Test
    void checkUserExistence(){
        doReturn(Optional.empty()).when(repository).findById(Mockito.anyLong());

        UserException userException = assertThrows(
                UserException.class,
                () -> userService.checkUserExistence(Mockito.anyLong())
        );

        assertThat(USER_NOT_FOUND).isEqualTo(userException.getMessage());
    }

    @Test
    void loadUserByUsername() {
        User user = getUser();
        doReturn(Optional.of(user)).when(repository).findUserByEmail(AUTHOR_EMAIL);
        UserDetails expectedUserDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>()
        );

        UserDetails actualUserDetails = userService.loadUserByUsername(AUTHOR_EMAIL);

        assertThat(expectedUserDetails).isEqualTo(actualUserDetails);
    }

    @Test
    void getUserByEmail() {
        User user = getUser();
        doReturn(Optional.of(user)).when(repository).findUserByEmail(AUTHOR_EMAIL);

        User actualUser = userService.getUserByEmail(AUTHOR_EMAIL);

        assertThat(user).isEqualTo(actualUser);
        verify(repository).findUserByEmail(AUTHOR_EMAIL);
    }

    private User getUser() {
        return User
                .builder()
                .id(AUTHOR_ID)
                .email(AUTHOR_EMAIL)
                .password(PASSWORD)
                .name(AUTHOR_NAME)
                .build();
    }

    private UserReadDto getUserReadDto() {
        return UserReadDto
                .builder()
                .id(AUTHOR_ID)
                .email(AUTHOR_EMAIL)
                .name(AUTHOR_NAME)
                .build();
    }

    private UserCreateDto getUserCreateDto() {
        return UserCreateDto
                .builder()
                .email(AUTHOR_EMAIL)
                .password(PASSWORD)
                .name(AUTHOR_NAME)
                .build();
    }
}