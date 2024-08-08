package com.example.service;

import com.example.domain.dto.UserCreateDto;
import com.example.domain.dto.UserReadDto;
import com.example.http.exception.UserException;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;
import com.example.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Сервис, отвечающий за обработку пользователей в системе
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {
    private static final String USER_NOT_FOUND = "Пользователь (автор или исполнитель) по данному id не найден";
    private final UserMapper mapper;
    private final UserRepository repository;

    @Transactional
    public UserReadDto createUser(UserCreateDto userDto) {
        return Optional.of(userDto)
                .map(mapper::dtoToObject)
                .map(repository::save)
                .map(mapper::objectToDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    public User getUserById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }

    public void checkUserExistence(Long id){
        if(!repository.findById(id).isPresent()){
            throw new UserException(USER_NOT_FOUND);
        }
    }

    /**
     * метод, загружающий пользователя по email
     * @param email - email
     * @return UserDetails - одна из реализаций UserDetails
     * @throws UsernameNotFoundException - exception, если не удалось загрузить пользователя
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findUserByEmail(email)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        new ArrayList<>()
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user: " + email));
    }

    public User getUserByEmail(String email) {
        return repository.findUserByEmail(email)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }
}
