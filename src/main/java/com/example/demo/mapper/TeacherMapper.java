package com.example.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.example.demo.dto.TeacherDTO;
import com.example.demo.entity.Teacher;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    TeacherMapper INSTANCE = Mappers.getMapper(TeacherMapper.class);

    TeacherDTO toDTO(Teacher teacher);

    Teacher toEntity(TeacherDTO teacherDTO);

}
