package com.example.util;

import com.example.domain.entity.User;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * util-класс для работы с UserMapper и TaskMapper
 */
@Named("UserMapperUtil")
@Component
@RequiredArgsConstructor
public class UserMapperUtil {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    @Named("getUser")
    public User getUser(Long id) {
        return repository.findById(id)
                .orElseThrow();
    }

    @Named("encodePassword")
    public String encodePassword(String password){
        return passwordEncoder.encode(password);
    }
}
