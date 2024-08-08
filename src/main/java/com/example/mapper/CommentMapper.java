package com.example.mapper;

import com.example.domain.dto.CommentCreateDto;
import com.example.domain.dto.CommentReadDto;
import com.example.domain.entity.Comment;
import com.example.util.TaskMapperUtil;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.time.LocalDate;

/**
 * маппер для преобразования сущностей объекта Comment в dto и обратно
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        imports = LocalDate.class,
        uses = TaskMapperUtil.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CommentMapper {
    @Mapping(target = "createdDate", expression = "java(LocalDate.now())")
    @Mapping(target = "task", qualifiedByName = {"TaskMapperUtil", "getTask"}, source = "taskId")
    Comment dtoToObject(CommentCreateDto commentDto);
    CommentReadDto objectToDto(Comment comment);
}
