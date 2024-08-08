package com.example.http.controller;

import com.example.domain.dto.JwtRequestDto;
import com.example.domain.dto.JwtResponseDto;
import com.example.domain.dto.UserCreateDto;
import com.example.domain.dto.UserReadDto;
import com.example.service.AuthService;
import com.example.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "AuthController", description = "Контроллер для регистрации и аутентификации пользователя")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthRestController {
    private final AuthService authService;
    private final UserService userService;

    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Позволяет зарегистрировать нового пользователя в системе"
    )
    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public UserReadDto registerNewUser(@Valid @RequestBody UserCreateDto userDto) {
        return userService.createUser(userDto);
    }

    @Operation(
            summary = "Создание токена авторизации",
            description = "Позволяет создать JWT токен для авторизации в системе"
    )
    @PostMapping("/auth")
    @ResponseStatus(HttpStatus.CREATED)
    public JwtResponseDto createAuthToken(@Valid @RequestBody JwtRequestDto jwtRequestDto) {
        return authService.createAuthToken(jwtRequestDto);
    }
}
