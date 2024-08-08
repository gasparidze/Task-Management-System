package com.example.mapper;

import com.example.domain.dto.TaskCreateEditDto;
import com.example.domain.dto.TaskReadDto;
import com.example.domain.entity.Task;
import com.example.util.UserMapperUtil;
import org.mapstruct.*;

import java.time.LocalDate;


/**
 * маппер для преобразования сущностей объекта Task в dto и обратно
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        imports = LocalDate.class,
        uses = {UserMapper.class, UserMapperUtil.class, CommentMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TaskMapper {
    @Mapping(target = "createdDate", expression = "java(LocalDate.now())")
    @Mapping(target = "performer", qualifiedByName = {"UserMapperUtil", "getUser"}, source = "performerId")
    Task dtoToObject(TaskCreateEditDto taskDto);
    TaskReadDto objectToDto(Task task);
}

