package com.example.service;

import com.example.domain.dto.JwtRequestDto;
import com.example.domain.dto.JwtResponseDto;
import com.example.domain.entity.User;
import com.example.http.exception.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;


/**
 * Сервис, отвечающий за аутентификацию клиентов в системе
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    /**
     * метод, аутентифицирующий клиента
     * @param jwtRequestDto - dto-объект, содержащий необходимую информацию для аутентификации
     * @return JwtResponseDto
     */
    public JwtResponseDto createAuthToken(JwtRequestDto jwtRequestDto) {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(
                    jwtRequestDto.getEmail(),
                    jwtRequestDto.getPassword())
            );
        } catch (BadCredentialsException e){
            throw new AuthException("Неверный логин или пароль");
        }

        User client = userService.getUserByEmail(jwtRequestDto.getEmail());
        String jwt = jwtService.generateToken(client);

        return new JwtResponseDto(jwt);
    }
}
