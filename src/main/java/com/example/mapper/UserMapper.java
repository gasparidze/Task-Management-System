package com.example.mapper;

import com.example.domain.dto.UserCreateDto;
import com.example.domain.dto.UserReadDto;
import com.example.domain.entity.User;
import com.example.util.UserMapperUtil;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * маппер для преобразования сущностей объекта User в dto и обратно
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = UserMapperUtil.class)
public interface UserMapper {
    @Mapping(target = "password", qualifiedByName = {"UserMapperUtil", "encodePassword"})
    User dtoToObject(UserCreateDto userDto);
    UserReadDto objectToDto(User user);
}
